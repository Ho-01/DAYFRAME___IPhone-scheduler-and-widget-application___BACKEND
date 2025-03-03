package com.BUS.DayFrame.service;

import com.BUS.DayFrame.dto.Response.TokenResponse;
import com.BUS.DayFrame.domain.RefreshToken;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.repository.RefreshTokenRepository;
import com.BUS.DayFrame.repository.UserRepository;
import com.BUS.DayFrame.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public TokenResponse login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("유저를 찾을 수 없습니다");
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("유효하지않은 비밀번호");
        }

        // JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        // DB에 refresh token 저장 (이미 존재하면 업데이트)
        RefreshToken tokenEntity = refreshTokenRepository.findByUser(user)
                .orElse(new RefreshToken());
        tokenEntity.setUser(user);
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpirationTime(LocalDateTime.now().plusSeconds(jwtUtil.getRefreshExpirationInSeconds()));
        refreshTokenRepository.save(tokenEntity);

        return new TokenResponse(true, accessToken, refreshToken);
    }

    public void logout(String token) {
        // 로그아웃 시 DB에 저장된 refresh token을 삭제&무효화 가능함
        // 토큰 값으로 조회 후 삭제하는 방식인데 필요한가..?
        refreshTokenRepository.findByRefreshToken(token).ifPresent(refreshTokenRepository::delete);
    }

    public TokenResponse refreshToken(String providedRefreshToken) {
        if (!jwtUtil.validateToken(providedRefreshToken)) {
            throw new RuntimeException("유효하지 않거나 만료된 토큰");
        }

        Optional<RefreshToken> refreshTokenEntityOpt = refreshTokenRepository.findByRefreshToken(providedRefreshToken);
        if (refreshTokenEntityOpt.isEmpty() ||
                refreshTokenEntityOpt.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("유효하지 않거나 만료된 토큰");
        }

        User user = refreshTokenEntityOpt.get().getUser();
        String newAccessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);

        // 갱신된 refresh token DB에 업데이트
        RefreshToken tokenEntity = refreshTokenEntityOpt.get();
        tokenEntity.setRefreshToken(newRefreshToken);
        tokenEntity.setExpirationTime(LocalDateTime.now().plusSeconds(jwtUtil.getRefreshExpirationInSeconds()));
        refreshTokenRepository.save(tokenEntity);

        return new TokenResponse(true, newAccessToken, newRefreshToken);
    }
}

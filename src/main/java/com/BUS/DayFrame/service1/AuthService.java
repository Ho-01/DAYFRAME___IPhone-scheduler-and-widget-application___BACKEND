package com.BUS.DayFrame.service;

import com.BUS.DayFrame.dto.response.TokenResponse;
import com.BUS.DayFrame.domain.RefreshToken;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.repository1.RefreshTokenRepository;
import com.BUS.DayFrame.repository1.UserRepository;
import com.BUS.DayFrame.security.util.JwtTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    public TokenResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("not found user"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("password not matched");
        }

        // ✅ JWT 토큰 생성
        String accessToken = jwtTokenUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getEmail());

        // ✅ Refresh Token 저장 또는 업데이트
        RefreshToken tokenEntity = refreshTokenRepository.findByUser(user)
                .orElse(new RefreshToken(user, refreshToken, LocalDateTime.now().plusSeconds(jwtTokenUtil.getRefreshExpirationInSeconds())));

        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpirationTime(LocalDateTime.now().plusSeconds(jwtTokenUtil.getRefreshExpirationInSeconds()));

        refreshTokenRepository.save(tokenEntity);

        return new TokenResponse(true, accessToken, refreshToken);
    }


    // ✅ 로그아웃 (Refresh Token 삭제)
    public void logout(String token) {
        refreshTokenRepository.findByRefreshToken(token).ifPresent(refreshTokenRepository::delete);
    }


    public TokenResponse refreshToken(String refreshToken) {

        RefreshToken tokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("not valid refresh token"));


        if (tokenEntity.getExpirationTime().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(tokenEntity);
            throw new RuntimeException("Refresh Token is expired");
        }


        User user = tokenEntity.getUser();
        if (user == null) {
            refreshTokenRepository.delete(tokenEntity);
            throw new RuntimeException("not found user");
        }


        refreshTokenRepository.delete(tokenEntity);
        String newAccessToken = jwtTokenUtil.generateAccessToken(user.getEmail());
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(user.getEmail());

        // 5️⃣ 새로운 Refresh Token 저장
        RefreshToken newToken = new RefreshToken(user, newRefreshToken, LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(newToken);

        return new TokenResponse(true, newAccessToken, newRefreshToken);
    }

}

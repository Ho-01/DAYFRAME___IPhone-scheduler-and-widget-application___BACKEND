package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.RefreshToken;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.request.RefreshTokenRequestDTO;
import com.BUS.DayFrame.dto.response.AccessTokenResponseDTO;
import com.BUS.DayFrame.dto.response.LoginResponseDTO;
import com.BUS.DayFrame.repository.RefreshTokenRepository;
import com.BUS.DayFrame.repository.UserRepository;
import com.BUS.DayFrame.security.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        User user = userOptional.get();
        String accessToken = jwtTokenUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getEmail());

        refreshTokenRepository.deleteByEmail(user.getEmail()); // 기존 Refresh Token 삭제
        refreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken, LocalDateTime.now().plusDays(7)));

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    @Transactional
    public Map<String, String> refreshAccessToken(String refreshToken) {
        // DB에서 Refresh Token 조회
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Refresh Token입니다."));

        // Refresh Token이 만료되었는지 확인
        if (storedToken.getExpireDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(storedToken); // 만료된 토큰 삭제
            throw new IllegalArgumentException("Refresh Token이 만료되었습니다. 다시 로그인하세요.");
        }

        // ✅ 새로운 Access Token & Refresh Token 발급
        String newAccessToken = jwtTokenUtil.generateAccessToken(storedToken.getEmail());
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(storedToken.getEmail());

        // 🔥 기존 Refresh Token 삭제 후 새 Refresh Token 저장
        refreshTokenRepository.delete(storedToken);
        refreshTokenRepository.save(new RefreshToken(storedToken.getEmail(), newRefreshToken, LocalDateTime.now().plusDays(7)));

        // 🎯 새로운 토큰 반환
        return Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        );
    }


    @Transactional
    public void logout(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Refresh Token입니다."));

        refreshTokenRepository.delete(storedToken); // Refresh Token 삭제
    }
}

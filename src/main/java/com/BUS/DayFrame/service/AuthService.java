package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.RefreshToken;
import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.response.LoginResponseDTO;
import com.BUS.DayFrame.dto.response.TokenResponseDTO;
import com.BUS.DayFrame.repository.RefreshTokenRepository;
import com.BUS.DayFrame.security.service.CustomUserDetailsService;
import com.BUS.DayFrame.security.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    // 로그인
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        String accessToken = jwtTokenUtil.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails.getUsername());

        // 기존 Refresh Token 삭제 후 새로 저장
        refreshTokenRepository.deleteByEmail(userDetails.getUsername());
        refreshTokenRepository.save(new RefreshToken(userDetails.getUsername(), refreshToken, LocalDateTime.now().plusDays(7)));

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    // 토큰 재발급 (TokenResponseDTO 반환)
    @Transactional
    public TokenResponseDTO refreshAccessToken(String email) {
        RefreshToken storedToken = refreshTokenRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Refresh Token입니다."));

        if (storedToken.getExpireDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new IllegalArgumentException("토큰이 만료되었습니다. 다시 로그인하세요.");
        }

        // 사용자 정보 조회
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        String newAccessToken = jwtTokenUtil.generateAccessToken(userDetails.getUsername());
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails.getUsername());

        // 기존 Refresh Token 삭제 후 새로 저장
        refreshTokenRepository.delete(storedToken);
        refreshTokenRepository.save(new RefreshToken(userDetails.getUsername(), newRefreshToken, LocalDateTime.now().plusDays(7)));

        return new TokenResponseDTO(newAccessToken, newRefreshToken);
    }

    // 로그아웃
    @Transactional
    public void logout(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }
}

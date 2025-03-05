package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.request.RefreshTokenRequestDTO;
import com.BUS.DayFrame.dto.response.AccessTokenResponseDTO;
import com.BUS.DayFrame.dto.response.LoginResponseDTO;
import com.BUS.DayFrame.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 로그인 API (JWT 발급)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    // Refresh Token을 사용하여 Access Token 갱신 (수정중) / FE에서 요청
    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestHeader("Authorization") String refreshTokenHeader) {
        // Bearer 토큰 형식 확인
        if (!refreshTokenHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "잘못된 토큰 형식입니다."));
        }

        String refreshToken = refreshTokenHeader.substring(7); // "Bearer " 제거

        // 새로운 Access Token 및 Refresh Token 발급
        Map<String, String> tokens = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String refreshTokenHeader) {
        if (refreshTokenHeader == null || !refreshTokenHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token 형식입니다.");
        }
        String refreshToken = refreshTokenHeader.substring(7); // "Bearer " 제거 후 토큰 추출
        authService.logout(refreshToken);
        return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
    }
}

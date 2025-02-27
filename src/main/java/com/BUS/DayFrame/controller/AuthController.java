package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.repository.RefreshTokenJpaRepository;
import com.BUS.DayFrame.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RefreshTokenJpaRepository refreshTokenJpaRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );

        String accessToken = jwtTokenUtil.generateAccessToken(loginRequestDTO.getEmail());
        String refreshToken = jwtTokenUtil.generateRefreshToken(loginRequestDTO.getEmail());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken
                )
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@AuthenticationPrincipal UserDetails userDetails) {
        refreshTokenJpaRepository.deleteByEmail(userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
                "success", true
        ));
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> tokenRefresh(@AuthenticationPrincipal UserDetails userDetails) {
        String accessToken = jwtTokenUtil.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken
                )
        ));
    }
}
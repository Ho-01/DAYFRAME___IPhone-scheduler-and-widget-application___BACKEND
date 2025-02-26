package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.LoginRequestDTO;
import com.BUS.DayFrame.repository.RefreshTokenJpaRepository;
import com.BUS.DayFrame.security.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
        String usernameWithId = authentication.getName();
        Long userId = Long.parseLong(usernameWithId.split(":")[0]);

        String accessToken = jwtTokenUtil.generateAccessToken(userId);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken
                )
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");

        jwtTokenUtil.validateToken(jwtToken);

        Long userId = jwtTokenUtil.extractUserId(jwtToken);
        refreshTokenJpaRepository.deleteById(userId);

        return ResponseEntity.ok(Map.of(
                "success", true
        ));
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> tokenRefresh(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");

        jwtTokenUtil.validateToken(jwtToken);

        Long userId = jwtTokenUtil.extractUserId(jwtToken);
        String accessToken = jwtTokenUtil.generateAccessToken(userId);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken
                )
        ));
    }
}
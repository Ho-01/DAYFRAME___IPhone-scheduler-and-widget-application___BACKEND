package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.response.TokenResponseDTO;
import com.BUS.DayFrame.repository.RefreshTokenJpaRepository;
import com.BUS.DayFrame.security.util.JwtTokenUtil;
import com.BUS.DayFrame.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        TokenResponseDTO tokenResponseDTO = authService.login(loginRequestDTO);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", tokenResponseDTO
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@AuthenticationPrincipal UserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
                "success", true
        ));
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> tokenRefresh(@AuthenticationPrincipal UserDetails userDetails) {
        TokenResponseDTO tokenResponseDTO = authService.tokenRefresh(userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", tokenResponseDTO
        ));
    }
}
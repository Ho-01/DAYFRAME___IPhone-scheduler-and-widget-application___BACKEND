package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.response.ApiResponseDTO;
import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.response.TokenResponseDTO;

import com.BUS.DayFrame.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponseDTO<TokenResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ApiResponseDTO.success(authService.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/logout")
    public ApiResponseDTO<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return ApiResponseDTO.success("로그아웃이 완료되었습니다.");
    }

    @PostMapping("/token")
    public ApiResponseDTO<TokenResponseDTO> refreshToken(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponseDTO.success(authService.refreshToken(userDetails.getUsername()));
    }
}


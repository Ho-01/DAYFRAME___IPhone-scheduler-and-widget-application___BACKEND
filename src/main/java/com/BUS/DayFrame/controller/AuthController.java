package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.exception.ErrorResponse;
import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.response.TokenResponse;

import com.BUS.DayFrame.service1.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            TokenResponse tokenResponse = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(tokenResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("invalid_credentials", "email or password is incorrect"));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(new ErrorResponse("missing_token", "missing_token"));
        }
        token = token.substring(7);
        authService.logout(token);


        return ResponseEntity.ok().body("로그아웃이 완료되었습니다.");
    }



    @PostMapping("/token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(new ErrorResponse("missing_token", "missing_token"));
        }
        token = token.substring(7);
        try {
            TokenResponse tokenResponse = authService.refreshToken(token);
            return ResponseEntity.ok(tokenResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("invalid_token", e.getMessage()));
        }
    }
}

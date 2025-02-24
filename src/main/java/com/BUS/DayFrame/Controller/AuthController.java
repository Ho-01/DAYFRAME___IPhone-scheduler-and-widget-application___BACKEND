package com.BUS.DayFrame.Controller;

import com.BUS.DayFrame.DTO.ErrorResponse;
import com.BUS.DayFrame.DTO.LoginRequest;
import com.BUS.DayFrame.DTO.SuccessResponse;
import com.BUS.DayFrame.DTO.TokenResponse;

import com.BUS.DayFrame.Service.AuthService;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            TokenResponse tokenResponse = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(tokenResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("invalid_credentials", "이메일 또는 비밀번호 오류"));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(new ErrorResponse("missing_token", "토큰이 제공되지 않았습니다."));
        }
        token = token.substring(7);
        authService.logout(token);
        return ResponseEntity.ok(new SuccessResponse(true));
    }


    @PostMapping("/token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(new ErrorResponse("missing_token", "토큰이 제공되지 않았습니다."));
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

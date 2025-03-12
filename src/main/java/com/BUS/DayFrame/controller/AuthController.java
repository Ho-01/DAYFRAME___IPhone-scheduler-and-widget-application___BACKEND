package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.response.ApiResponseDTO;
import com.BUS.DayFrame.dto.response.LoginResponseDTO;
import com.BUS.DayFrame.dto.response.TokenResponseDTO;
import com.BUS.DayFrame.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    // 로그인 API (JWT 발급)
    @PostMapping("/login")
    public ApiResponseDTO<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ApiResponseDTO.success(authService.login(loginRequestDTO));
    }

    // 토큰 갱신 (인증된 사용자 정보 활용)
    @PostMapping("/token")
    public ApiResponseDTO<TokenResponseDTO> refreshAccessToken(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponseDTO.success(authService.tokenRefresh(userDetails.getUsername()));
    }

    // 로그아웃 (인증된 사용자 정보 활용)
    @PostMapping("/logout")
    public ApiResponseDTO<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return ApiResponseDTO.success("로그아웃이 완료되었습니다.");
    }
}
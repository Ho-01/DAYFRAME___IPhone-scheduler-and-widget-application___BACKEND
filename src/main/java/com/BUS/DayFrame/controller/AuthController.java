package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.request.OAuthLoginDTO;
import com.BUS.DayFrame.dto.response.ApiResponseDTO;
import com.BUS.DayFrame.dto.response.TokenResponseDTO;
import com.BUS.DayFrame.security.service.CustomUserDetails;
import com.BUS.DayFrame.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    // 로그인 API (JWT 발급)
    @PostMapping("/login")
    public ApiResponseDTO<TokenResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ApiResponseDTO.success(authService.login(loginRequestDTO));
    }

    // 토큰 갱신 (인증된 사용자 정보 활용)
    @PostMapping("/token")
    public ApiResponseDTO<TokenResponseDTO> refreshAccessToken(@RequestHeader("Authorization") String refreshTokenHeader) {
        return ApiResponseDTO.success(authService.tokenRefresh(refreshTokenHeader));
    }

    // 로그아웃 (인증된 사용자 정보 활용)
    @PostMapping("/logout")
    public ApiResponseDTO<String> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.logout(userDetails.getUserId());
        return ApiResponseDTO.success("로그아웃이 완료되었습니다.");
    }
    @PostMapping("/google")
    public ApiResponseDTO<TokenResponseDTO> googleLogin(@RequestBody OAuthLoginDTO dto) {
        return ApiResponseDTO.success(authService.googleLogin(dto.getIdToken()));
    }
// 카카오 email받아오게해줘ㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓㅓ
    @PostMapping("/kakao")
    public ApiResponseDTO<TokenResponseDTO>kakaoLogin(@RequestBody OAuthLoginDTO dto) {
        return ApiResponseDTO.success(authService.kakaoLogin(dto.getIdToken()));
    }

}

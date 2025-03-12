package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.UserCreateDTO;
import com.BUS.DayFrame.dto.request.UserUpdateDTO;
import com.BUS.DayFrame.dto.response.ApiResponseDTO;
import com.BUS.DayFrame.dto.response.UserResponseDTO;
import com.BUS.DayFrame.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ApiResponseDTO<?> register(@RequestBody UserCreateDTO userCreateDTO) {
        userService.register(userCreateDTO);
        return ApiResponseDTO.success("회원가입이 완료되었습니다.");
    }

    // 현재 로그인한 사용자 정보 조회
    @GetMapping("/info")
    public ApiResponseDTO<UserResponseDTO> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDTO userInfo = userService.getUserInfo(userDetails.getUsername());
        return ApiResponseDTO.success(userInfo);
    }

    // 현재 로그인한 사용자 정보 수정
    @PutMapping("/info")
    public ApiResponseDTO<UserResponseDTO> updateUserInfo(
            @RequestBody UserUpdateDTO userUpdateDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        UserResponseDTO updatedUser = userService.updateUserInfo(userDetails.getUsername(), userUpdateDTO);
        return ApiResponseDTO.success(updatedUser);
    }

    // 현재 로그인한 사용자 계정 삭제
    @DeleteMapping("/info")
    public ApiResponseDTO<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails.getUsername());
        return ApiResponseDTO.success("회원 탈퇴 성공");
    }
}

package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.UserCreateDTO;
import com.BUS.DayFrame.dto.request.UserUpdateDTO;
import com.BUS.DayFrame.dto.response.ApiResponseDTO;
import com.BUS.DayFrame.dto.response.UserInfoResponseDTO;
import com.BUS.DayFrame.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ApiResponseDTO<String> register(@Valid @RequestBody UserCreateDTO request) {
        userService.registerUser(request);
        return ApiResponseDTO.success("회원가입이 완료되었습니다.");
    }


    @GetMapping("/info")
    public ApiResponseDTO<UserInfoResponseDTO> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponseDTO.success(userService.getUserByEmail(userDetails.getUsername()));
    }


    @PutMapping("/info")
    public ApiResponseDTO<UserInfoResponseDTO> updateUser(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestBody UserUpdateDTO updateDTO) {
        return ApiResponseDTO.success(userService.updateUser(userDetails.getUsername(), updateDTO));
    }


    @DeleteMapping("/info")
    public ApiResponseDTO<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails.getUsername());
        return ApiResponseDTO.success("사용자 정보가 삭제되었습니다.");
    }
}

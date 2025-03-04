package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.request.UserCreateDTO;
import com.BUS.DayFrame.dto.response.UserResponseDTO;
import com.BUS.DayFrame.dto.request.UserUpdateDTO;
import com.BUS.DayFrame.security.util.JwtTokenUtil;
import com.BUS.DayFrame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(UserCreateDTO userCreateDTO){
        userService.register(userCreateDTO);
        return ResponseEntity.ok(Map.of(
                "success", true
        ));
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal UserDetails userDetails){
        UserResponseDTO userResponseDTO = userService.getUserInfo(userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", userResponseDTO
        ));
    }

    @PutMapping("/info")
    public ResponseEntity<Map<String, Object>> updateUserInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserUpdateDTO userUpdateDTO) {
        UserResponseDTO userResponseDTO = userService.updateUserInfo(userDetails.getUsername(), userUpdateDTO);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", userResponseDTO
        ));
    }

    @DeleteMapping("/info")
    public ResponseEntity<Map<String, Object>> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "사용자가 성공적으로 삭제되었습니다"
        ));
    }
}

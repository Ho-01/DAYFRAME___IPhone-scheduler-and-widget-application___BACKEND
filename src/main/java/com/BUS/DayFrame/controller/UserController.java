package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.UserRegisterDTO;
import com.BUS.DayFrame.dto.UserResponseDTO;
import com.BUS.DayFrame.dto.UserUpdateDTO;
import com.BUS.DayFrame.security.JwtTokenUtil;
import com.BUS.DayFrame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(UserRegisterDTO userRegisterDTO){
        User savedUser = userService.register(userRegisterDTO);

        String accessToken = jwtTokenUtil.generateAccessToken(savedUser.getId());
        String refreshToken = jwtTokenUtil.generateRefreshToken(savedUser.getId());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken
                )
        ));
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") String token){
        String jwtToken = token.replace("Bearer ", "");

        jwtTokenUtil.validateToken(jwtToken);
        Long userId = jwtTokenUtil.extractUserId(jwtToken);

        UserResponseDTO user = userService.getUserInfo(userId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", user
        ));
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<Map<String, Object>> updateUserInfo(
            @RequestHeader("Authorization") String token,
            @PathVariable("user_id") Long pathUserId,
            @RequestBody UserUpdateDTO userUpdateDTO) {

        String jwtToken = token.replace("Bearer ", "");
        jwtTokenUtil.validateToken(jwtToken);
        Long tokenUserId = jwtTokenUtil.extractUserId(jwtToken);

        if (!tokenUserId.equals(pathUserId)) {
            throw new AccessDeniedException("User is not authorized");
        }

        UserResponseDTO user = userService.updateUserInfo(pathUserId, userUpdateDTO);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", user
        ));
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<Map<String, Object>> deleteUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("user_id") Long pathUserId) {

        String jwtToken = token.replace("Bearer ", "");
        jwtTokenUtil.validateToken(jwtToken);
        Long tokenUserId = jwtTokenUtil.extractUserId(jwtToken);

        if (!tokenUserId.equals(pathUserId)) {
            throw new AccessDeniedException("User is not authorized");
        }

        userService.deleteUser(pathUserId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "사용자가 성공적으로 삭제되었습니다"
        ));
    }
}

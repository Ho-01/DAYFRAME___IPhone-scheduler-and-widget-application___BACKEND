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
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(UserCreateDTO userCreateDTO){
        User savedUser = userService.register(userCreateDTO);

        String accessToken = jwtTokenUtil.generateAccessToken(savedUser.getEmail());
        String refreshToken = jwtTokenUtil.generateRefreshToken(savedUser.getEmail());

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
        String email = jwtTokenUtil.extractEmail(jwtToken);
        Long userId = userService.getUserIdByEmail(email);

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
        String email = jwtTokenUtil.extractEmail(jwtToken);
        Long tokenUserId = userService.getUserIdByEmail(email);

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
        String email = jwtTokenUtil.extractEmail(jwtToken);
        Long tokenUserId = userService.getUserIdByEmail(email);

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

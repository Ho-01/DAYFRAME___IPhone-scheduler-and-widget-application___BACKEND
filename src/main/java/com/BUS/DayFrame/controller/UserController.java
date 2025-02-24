package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.UserRegisterDTO;
import com.BUS.DayFrame.dto.UserResponseDTO;
import com.BUS.DayFrame.security.JwtTokenUtil;
import com.BUS.DayFrame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

//    @GetMapping("/info")
//    public ResponseEntity<Map<String, Object>> getUserInfo(){
//        UserResponseDTO user = userService.getUserInfo(userId);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("data", user);
//
//        return ResponseEntity.ok(response);
//    }

//    @PutMapping("/{user_id}")
//    public updateUserInfo
//
//    @DeleteMapping("/{user_id}")
//    public deleteUser
}

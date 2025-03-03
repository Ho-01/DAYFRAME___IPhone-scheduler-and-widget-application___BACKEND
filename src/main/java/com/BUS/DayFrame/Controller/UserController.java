package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.Error.ErrorResponse;
import com.BUS.DayFrame.dto.Response.TokenResponse;
import com.BUS.DayFrame.dto.Response.UserInfoResponse;
import com.BUS.DayFrame.dto.Request.UserCreateDTO;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCreateDTO request) {
        try {
            TokenResponse tokenResponse = userService.registerUser(request);
            return ResponseEntity.ok(tokenResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("가입실패", e.getMessage()));
        }
    }
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(new ErrorResponse("unauthorized", "로그인이 필요합니다."));
        }

        User user = userService.getUserByEmail(userDetails.getUsername());
        UserInfoResponse response = new UserInfoResponse(user);

        return ResponseEntity.ok(response);
    }
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody Map<String, String> requestBody) {

        String password = requestBody.get("password");
        String name = requestBody.get("name");

        User updatedUser = userService.updateUser(userId, password, name);
        return ResponseEntity.ok(new UserInfoResponse(updatedUser));
    }



    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
    }




package com.BUS.DayFrame.controller1;

import com.BUS.DayFrame.exception.ErrorResponse;
import com.BUS.DayFrame.dto.response.UserInfoResponse;
import com.BUS.DayFrame.dto.request.UserCreateDTO;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.service.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ErrorResponse> register(@Valid @RequestBody UserCreateDTO request) {
        try {
            userService.registerUser(request);
            return ResponseEntity.ok(ErrorResponse.success("create success"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ErrorResponse.error("가입실패", e.getMessage()));
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


    @PutMapping("/info")
    public ResponseEntity<?> updateUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> requestBody) {

        if (userDetails == null) {
            return ResponseEntity.status(403).body(new ErrorResponse("forbidden", "authentication required"));
        }

        // JWT 토큰에서 로그인한 유저 정보 가져오기
        User user = userService.getUserByEmail(userDetails.getUsername());

        String password = requestBody.get("password");
        String name = requestBody.get("name");

        // 사용자 정보 업데이트
        User updatedUser = userService.updateUser(user.getId(), password, name);

        return ResponseEntity.ok(new UserInfoResponse(updatedUser));
    }



    @DeleteMapping("/info")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(403).body(new ErrorResponse("forbidden", "authentication required"));
        }

        User user = userService.getUserByEmail(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("not_found", "not_found user"));
        }

        System.out.println("delete " + user.getEmail());
        userService.deleteUser(user.getId());

        return ResponseEntity.ok().body(new ErrorResponse("success", "delte user"));
    }

}




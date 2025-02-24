package com.BUS.DayFrame.Controller;

import com.BUS.DayFrame.DTO.ErrorResponse;
import com.BUS.DayFrame.DTO.TokenResponse;
import com.BUS.DayFrame.DTO.UserRegistrationRequest;
import com.BUS.DayFrame.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest request) {
        try {
            TokenResponse tokenResponse = userService.registerUser(request);
            return ResponseEntity.ok(tokenResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("registration_failed", e.getMessage()));
        }
    }
    //info 추가예정
    //update 추가예정
    //delete 추가예정
}

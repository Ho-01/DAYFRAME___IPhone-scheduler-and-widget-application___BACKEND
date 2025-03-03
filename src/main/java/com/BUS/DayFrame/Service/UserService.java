package com.BUS.DayFrame.service;

import com.BUS.DayFrame.dto.Response.TokenResponse;
import com.BUS.DayFrame.dto.Request.UserCreateDTO;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.repository.UserRepository;
import com.BUS.DayFrame.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public TokenResponse registerUser(UserCreateDTO request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("사용중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encodedPassword, request.getName());
        userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        return new TokenResponse(true, accessToken, refreshToken);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));// 근데 에러코드를 모두 같은거로 만든다고 했었던거같은데
    }


    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    public User updateUser(Long userId, String password, String name) {
        String email = userRepository.findEmailById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 이메일을 찾을 수 없습니다."));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.updateUser(password, name, passwordEncoder);
        return userRepository.save(user);
    }




    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }
}


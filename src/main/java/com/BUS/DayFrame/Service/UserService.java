package com.BUS.DayFrame.service;

import com.BUS.DayFrame.dto.response.TokenResponse;
import com.BUS.DayFrame.dto.request.UserCreateDTO;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.repository.RefreshTokenRepository;
import com.BUS.DayFrame.repository.UserRepository;
import com.BUS.DayFrame.util.JwtTokenUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;


    @PersistenceContext
    private EntityManager entityManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public TokenResponse registerUser(UserCreateDTO request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("already exists user_email");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encodedPassword, request.getName());
        userRepository.save(user);

        String accessToken = jwtTokenUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getEmail());

        return new TokenResponse(true, accessToken, refreshToken);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("not found user"));// 근데 에러코드를 모두 같은거로 만든다고 했었던거같은데
    }


    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found user"));
    }

    public User updateUser(Long userId, String password, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("not found user"));

        user.updateUser(password, name, passwordEncoder);
        return userRepository.save(user);
    }





    @Transactional
    public void deleteUser(Long userId) {

        refreshTokenRepository.deleteByUserId(userId);


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("not found user"));
        userRepository.delete(user);
    }


}


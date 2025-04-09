package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.request.UserCreateDTO;
import com.BUS.DayFrame.dto.response.TokenResponseDTO;
import com.BUS.DayFrame.repository.RefreshTokenJpaRepository;
import com.BUS.DayFrame.repository.UserJpaRepository;
import com.BUS.DayFrame.security.util.JwtTokenUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenJpaRepository refreshTokenJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    private Long userId;
    private final String email = "test@example.com";
    private final String password = "password";
    @BeforeEach
    void setUp() {
        User testUser = new User(email,passwordEncoder.encode(password),"testUser");
        userJpaRepository.save(testUser);
        userId = userJpaRepository.findByEmail(email).get().getId();
    }

    @Test
    void login() {
        TokenResponseDTO tokenResponseDTO = authService.login(new LoginRequestDTO(email, password));
        Assertions.assertThat(tokenResponseDTO.getAccessToken()).isNotEmpty();
        Assertions.assertThat(tokenResponseDTO.getRefreshToken()).isNotEmpty();
        Assertions.assertThat(jwtTokenUtil.extractUserId(tokenResponseDTO.getAccessToken())).isEqualTo(userId);
        Assertions.assertThat(jwtTokenUtil.extractUserId(tokenResponseDTO.getRefreshToken())).isEqualTo(userId);
    }

    @Test
    void logout() {
        authService.login(new LoginRequestDTO(email, password));
        Assertions.assertThat(refreshTokenJpaRepository.findByUserId(userId).isPresent()).isEqualTo(true);
        authService.logout(userId);
        Assertions.assertThat(refreshTokenJpaRepository.findByUserId(userId).isPresent()).isEqualTo(false);
    }

    @Test
    void tokenRefresh() {
        authService.login(new LoginRequestDTO(email, password));
        Assertions.assertThat(refreshTokenJpaRepository.findByUserId(userId).isPresent()).isEqualTo(true);
//        authService.tokenRefresh(userId);
//        Assertions.assertThat(refreshTokenJpaRepository.findByUserId(userId).isPresent()).isEqualTo(true);
    }
}
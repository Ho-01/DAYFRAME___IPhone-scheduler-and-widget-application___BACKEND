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

    private final String email = "test@example.com";
    private final String password = "password";
    @BeforeEach
    void setUp() {
        User testUser = new User(email,passwordEncoder.encode(password),"testUser");
        userJpaRepository.save(testUser);
    }

    @Test
    void login() {
        TokenResponseDTO tokenResponseDTO = authService.login(new LoginRequestDTO(email, password));
        Assertions.assertThat(tokenResponseDTO.getAccessToken()).isNotEmpty();
        Assertions.assertThat(tokenResponseDTO.getRefreshToken()).isNotEmpty();
        Assertions.assertThat(jwtTokenUtil.extractEmail(tokenResponseDTO.getAccessToken())).isEqualTo(email);
        Assertions.assertThat(jwtTokenUtil.extractEmail(tokenResponseDTO.getRefreshToken())).isEqualTo(email);
    }

    @Test
    void logout() {
        authService.login(new LoginRequestDTO(email, password));
        Assertions.assertThat(refreshTokenJpaRepository.findByEmail(email).isPresent()).isEqualTo(true);
        authService.logout(email);
        Assertions.assertThat(refreshTokenJpaRepository.findByEmail(email).isPresent()).isEqualTo(false);
    }

    @Test
    void tokenRefresh() {
        authService.login(new LoginRequestDTO(email, password));
        Assertions.assertThat(refreshTokenJpaRepository.findByEmail(email).isPresent()).isEqualTo(true);
        authService.tokenRefresh(email);
        Assertions.assertThat(refreshTokenJpaRepository.findByEmail(email).isPresent()).isEqualTo(true);
    }
}
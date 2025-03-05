package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.response.TokenResponseDTO;
import com.BUS.DayFrame.repository.RefreshTokenJpaRepository;
import com.BUS.DayFrame.repository.UserJpaRepository;
import com.BUS.DayFrame.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private RefreshTokenJpaRepository refreshTokenJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    private final String email = "test@example.com";
    private final String password = "testPassword";

    @BeforeEach
    void setUp() {
        userJpaRepository.save(new User(email, passwordEncoder.encode(password),"testUser"));
    }

    @Test
    void login() throws Exception{
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, password);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.token.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.token.refreshToken").isNotEmpty());
    }

    @Test
    void logout() throws Exception{
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, password);
        TokenResponseDTO tokenResponseDTO = authService.login(loginRequestDTO);

        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer " + tokenResponseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        Assertions.assertThat(refreshTokenJpaRepository.findByEmail(email)).isEmpty();
    }

    @Test
    void tokenRefresh() throws Exception{
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, password);
        TokenResponseDTO tokenResponseDTO = authService.login(loginRequestDTO);

        mockMvc.perform(post("/auth/token")
                        .header("Authorization", "Bearer " + tokenResponseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.token.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.token.refreshToken").isNotEmpty());
    }
}

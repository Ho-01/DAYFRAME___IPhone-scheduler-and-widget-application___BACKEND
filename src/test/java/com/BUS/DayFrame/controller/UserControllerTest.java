package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.request.UserCreateDTO;
import com.BUS.DayFrame.dto.request.UserUpdateDTO;
import com.BUS.DayFrame.dto.response.TokenResponseDTO;
import com.BUS.DayFrame.repository.UserJpaRepository;
import com.BUS.DayFrame.service.AuthService;
import com.BUS.DayFrame.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper; // JSON 변환용

    private final String email = "test@example.com";
    private String accessToken;

    @BeforeEach
    void setUp() {
        String password = "password";
        userService.register(new UserCreateDTO(email, password, "testUser"));
        TokenResponseDTO tokenResponse = authService.login(new LoginRequestDTO(email, password));
        accessToken = tokenResponse.getAccessToken();
    }

    @Test
    void register() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("newUser@example.com", "newPassword", "newName");

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        Assertions.assertThat(userJpaRepository.findByEmail(userCreateDTO.getEmail())).isPresent();
    }

    @Test
    void getUserInfo() throws Exception {
        // When & Then
        mockMvc.perform(get("/users/info")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value(email));
    }

    @Test
    void updateUserInfo() throws Exception {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("newPassword","newName");

        mockMvc.perform(put("/users/info")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value(userUpdateDTO.getName()));

        Assertions.assertThat(userJpaRepository.findByEmail(email).isPresent()).isEqualTo(true);
        Assertions.assertThat(userJpaRepository.findByEmail(email).get().getName()).isEqualTo(userUpdateDTO.getName());
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/info")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("사용자가 성공적으로 삭제되었습니다"));

        Assertions.assertThat(userJpaRepository.findByEmail(email)).isEmpty();
    }
}
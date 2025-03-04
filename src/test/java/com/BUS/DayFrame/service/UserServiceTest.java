package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.request.UserCreateDTO;
import com.BUS.DayFrame.dto.request.UserUpdateDTO;
import com.BUS.DayFrame.dto.response.UserResponseDTO;
import com.BUS.DayFrame.repository.UserJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest{
    @Autowired
    private UserService userService;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private User testUser;

    @BeforeEach
    void setup(){
        testUser = new User("testUser@email.com","testUserPassword","testUser");
        userJpaRepository.save(testUser);
    }
    @Test
    void register(){
        UserCreateDTO userCreateDTO = new UserCreateDTO("user@email.com","userPassword","user");
        userService.register(userCreateDTO);
        Assertions.assertThat(userJpaRepository.findByEmail(userCreateDTO.getEmail()).isPresent()).isEqualTo(true);
        User foundUser = userJpaRepository.findByEmail(userCreateDTO.getEmail()).get();
        Assertions.assertThat(foundUser.getEmail()).isEqualTo(userCreateDTO.getEmail());
        Assertions.assertThat(passwordEncoder.matches(userCreateDTO.getPassword(), foundUser.getPassword())).isEqualTo(true);
        Assertions.assertThat(foundUser.getName()).isEqualTo(userCreateDTO.getName());
    }

    @Test
    void getUserInfo(){
        UserResponseDTO userResponseDTO = userService.getUserInfo(testUser.getEmail());
        Assertions.assertThat(userResponseDTO.getId()).isEqualTo(testUser.getId());
        Assertions.assertThat(userResponseDTO.getEmail()).isEqualTo(testUser.getEmail());
        Assertions.assertThat(userResponseDTO.getName()).isEqualTo(testUser.getName());
        Assertions.assertThat(userResponseDTO.getCreatedAt()).isEqualTo(testUser.getCreatedAt());
        User foundUser = userJpaRepository.findByEmail(userResponseDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String foundPassword = foundUser.getPassword();
        Assertions.assertThat(foundPassword).isEqualTo(testUser.getPassword());
    }

    @Test
    void updateUserInfo(){
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("newPassword","newName");
        UserResponseDTO userResponseDTO = userService.updateUserInfo(testUser.getEmail(), userUpdateDTO);
        Assertions.assertThat(userResponseDTO.getId()).isEqualTo(testUser.getId());
        Assertions.assertThat(userResponseDTO.getEmail()).isEqualTo(testUser.getEmail());
        Assertions.assertThat(userResponseDTO.getName()).isEqualTo(userUpdateDTO.getName());
        User foundUser = userJpaRepository.findByEmail(userResponseDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String foundPassword = foundUser.getPassword();
        Assertions.assertThat(foundPassword).isEqualTo(userUpdateDTO.getPassword());
    }

    @Test
    void deleteUser(){
        Assertions.assertThat(userJpaRepository.findByEmail(testUser.getEmail()).isPresent()).isEqualTo(true);
        userService.deleteUser(testUser.getEmail());
        Assertions.assertThat(userJpaRepository.findByEmail(testUser.getEmail()).isPresent()).isEqualTo(false);
    }
}
package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserJpaRepositoryTest {
    @Autowired
    private UserJpaRepository userJpaRepository;
    private User testUser;
    @BeforeEach
    void setUp() {
        testUser = new User("test@email.com","password","testUser");
        userJpaRepository.save(testUser);
    }

    @Test
    void findByEmail() {
        Assertions.assertThat(userJpaRepository.findByEmail(testUser.getEmail()).isPresent()).isEqualTo(true);
        User foundUser =  userJpaRepository.findByEmail(testUser.getEmail()).get();
        Assertions.assertThat(foundUser.getEmail()).isEqualTo(testUser.getEmail());
        Assertions.assertThat(foundUser.getPassword()).isEqualTo(testUser.getPassword());
        Assertions.assertThat(foundUser.getName()).isEqualTo(testUser.getName());
    }
}
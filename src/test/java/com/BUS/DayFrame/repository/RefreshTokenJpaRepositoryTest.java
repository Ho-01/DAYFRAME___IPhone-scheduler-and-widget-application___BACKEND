package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.RefreshToken;
import com.BUS.DayFrame.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RefreshTokenJpaRepositoryTest {
    @Autowired
    private RefreshTokenJpaRepository refreshTokenJpaRepository;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        User user = new User("test@email.com", "testPassword", "testUser");
        refreshToken = new RefreshToken(user,"token", LocalDateTime.now());
        refreshTokenJpaRepository.save(refreshToken);
    }

    @Test
    void deleteByEmail() {
        refreshTokenJpaRepository.deleteByUser(refreshToken.getUser());
        Assertions.assertThat(refreshTokenJpaRepository.findById(refreshToken.getId()).isPresent()).isEqualTo(false);
    }
}
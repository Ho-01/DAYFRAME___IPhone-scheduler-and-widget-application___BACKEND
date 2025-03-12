package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.RefreshToken;
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
        refreshToken = new RefreshToken("test@email.com","token", LocalDateTime.now());
        refreshTokenJpaRepository.save(refreshToken);
    }

    @Test
    void deleteByEmail() {
        refreshTokenJpaRepository.deleteByEmail(refreshToken.getEmail());
        Assertions.assertThat(refreshTokenJpaRepository.findById(refreshToken.getId()).isPresent()).isEqualTo(false);
    }
}
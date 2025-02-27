package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByEmail(String email);
}

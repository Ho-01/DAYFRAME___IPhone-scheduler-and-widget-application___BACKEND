package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByEmail(String email);
    Optional<RefreshToken> findByEmail(String email);
}

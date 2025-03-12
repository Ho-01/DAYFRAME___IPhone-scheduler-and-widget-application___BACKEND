package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.RefreshToken;
import com.BUS.DayFrame.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);



    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user.id = :userId")
    void deleteByUserId(Long userId);
}

package com.BUS.DayFrame.Repository;

import com.BUS.DayFrame.Entity.RefreshToken;
import com.BUS.DayFrame.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}

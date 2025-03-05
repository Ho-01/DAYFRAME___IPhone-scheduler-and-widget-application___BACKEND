package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    //이메일이 중복되어있는지 확인용
    boolean existsByEmail(String email);

    @Query("SELECT u.email FROM User u WHERE u.id = :userId")
    Optional<String> findEmailById(@Param("userId") Long userId);

}

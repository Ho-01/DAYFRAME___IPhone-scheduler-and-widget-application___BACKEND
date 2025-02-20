package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}

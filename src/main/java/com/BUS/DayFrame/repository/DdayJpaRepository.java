package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.Dday;
import com.BUS.DayFrame.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DdayJpaRepository extends JpaRepository<Dday, Long> {
    List<Dday> findByUser(User user);
}

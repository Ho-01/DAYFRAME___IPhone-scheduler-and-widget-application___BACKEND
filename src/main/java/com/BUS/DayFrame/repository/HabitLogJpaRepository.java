package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.HabitLog;
import com.BUS.DayFrame.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitLogJpaRepository extends JpaRepository<HabitLog, Long> {

    List<HabitLog> findByHabit_User(User user);

}

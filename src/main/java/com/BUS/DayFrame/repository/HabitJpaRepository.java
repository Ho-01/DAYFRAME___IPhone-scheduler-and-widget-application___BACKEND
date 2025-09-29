package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.Habit;
import com.BUS.DayFrame.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitJpaRepository extends JpaRepository<Habit, Long> {

    List<Habit> findByUser(User user);
}

package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserIdAndStartDateTimeBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}

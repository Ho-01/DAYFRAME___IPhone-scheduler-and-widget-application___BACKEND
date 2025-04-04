package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.Schedule;
import com.BUS.DayFrame.domain.ScheduleTag;
import com.BUS.DayFrame.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleTagJpaRepository extends JpaRepository<ScheduleTag, Long> {
    List<ScheduleTag> findByTag(Tag tag);
}

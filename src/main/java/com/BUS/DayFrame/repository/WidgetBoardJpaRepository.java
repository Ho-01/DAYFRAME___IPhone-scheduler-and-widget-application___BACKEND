package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.WidgetBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WidgetBoardJpaRepository extends JpaRepository<WidgetBoard, Long> {
    List<WidgetBoard> findByUserId(Long userId);
}

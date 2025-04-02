package com.BUS.DayFrame.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class HabitLogResponseDTO {
    private Long id;
    private Long habitId;
    private String habitTitle;
    private LocalDate targetDate;
    private LocalDateTime completedAt;
    private Boolean success;
}

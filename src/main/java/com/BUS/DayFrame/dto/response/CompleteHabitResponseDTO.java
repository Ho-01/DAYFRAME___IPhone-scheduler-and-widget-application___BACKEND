package com.BUS.DayFrame.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CompleteHabitResponseDTO {
    private Long habitId;
    private LocalDateTime completedAt;
}

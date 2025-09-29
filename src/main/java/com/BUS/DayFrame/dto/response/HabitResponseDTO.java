package com.BUS.DayFrame.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class HabitResponseDTO {
    private Long id;
    private String habitTitle;
    private LocalDate startDate;
    private LocalDate endDate;     // nullable
    private String repeatDays;
}

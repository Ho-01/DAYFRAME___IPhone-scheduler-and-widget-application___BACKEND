package com.BUS.DayFrame.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class HabitCreateDTO {
    private String habitTitle;
    private LocalDate startDate;
    private LocalDate endDate; // nullable
    private String repeatDays;
}


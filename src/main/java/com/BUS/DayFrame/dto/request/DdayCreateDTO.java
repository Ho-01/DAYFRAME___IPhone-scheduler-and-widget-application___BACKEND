package com.BUS.DayFrame.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DdayCreateDTO {
    private String title;
    private LocalDate targetDate;
}

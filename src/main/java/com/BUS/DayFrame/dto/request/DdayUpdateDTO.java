package com.BUS.DayFrame.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DdayUpdateDTO {
    private String title;
    private LocalDate targetDate;
}

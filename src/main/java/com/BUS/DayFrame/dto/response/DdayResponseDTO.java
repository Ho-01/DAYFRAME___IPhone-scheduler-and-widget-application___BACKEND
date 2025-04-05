package com.BUS.DayFrame.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DdayResponseDTO {
    private Long id;
    private String title;
    private LocalDate targetDate;
}

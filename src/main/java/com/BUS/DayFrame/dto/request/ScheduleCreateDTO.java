package com.BUS.DayFrame.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ScheduleCreateDTO {
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Boolean allDay;
    private String location;
    private String description;
    private List<Long> tagIds;
}

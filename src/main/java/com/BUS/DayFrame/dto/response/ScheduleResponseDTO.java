package com.BUS.DayFrame.dto.response;

import com.BUS.DayFrame.domain.Schedule;
import com.BUS.DayFrame.domain.ScheduleTag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ScheduleResponseDTO {
    private Long id;
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Boolean allDay;
    private String location;
    private String description;
    private Boolean isDone;
    private List<Long> tagIds;

    public static ScheduleResponseDTO fromEntity(Schedule schedule) {
        List<Long> tagIds = new ArrayList<>();
        for(ScheduleTag scheduleTag : schedule.getScheduleTags()){
            tagIds.add(scheduleTag.getTag().getId());
        }
        return new ScheduleResponseDTO(
                schedule.getId(),
                schedule.getName(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime(),
                schedule.getAllDay(),
                schedule.getLocation(),
                schedule.getDescription(),
                schedule.getIsDone(),
                tagIds
        );
    }

    public static List<ScheduleResponseDTO> fromEntityList(List<Schedule> schedules) {
        List<ScheduleResponseDTO> scheduleResponseDTOList = new ArrayList<>();
        for(Schedule schedule : schedules){
            scheduleResponseDTOList.add(ScheduleResponseDTO.fromEntity(schedule));
        }
        return scheduleResponseDTOList;
    }
}

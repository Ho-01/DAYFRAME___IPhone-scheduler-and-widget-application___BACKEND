package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.ScheduleCreateDTO;
import com.BUS.DayFrame.dto.request.ScheduleUpdateDTO;
import com.BUS.DayFrame.dto.response.ApiResponseDTO;
import com.BUS.DayFrame.dto.response.ScheduleResponseDTO;
import com.BUS.DayFrame.security.service.CustomUserDetails;
import com.BUS.DayFrame.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/schedules")
    public ApiResponseDTO<ScheduleResponseDTO> createSchedule(
            @RequestBody ScheduleCreateDTO scheduleCreateDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ApiResponseDTO.success(scheduleService.saveSchedule(userDetails.getUserId(), scheduleCreateDTO));
    }

    @GetMapping("/schedules/period")
    public ApiResponseDTO<List<ScheduleResponseDTO>> getSchedulesByPeriod(
            @RequestParam(value = "period") String period,
            @RequestParam(value = "today")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime today,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ApiResponseDTO.success(scheduleService.findSchedulesByPeriod(userDetails.getUserId(), period, today));
    }

    @GetMapping("/schedules/range")
    public ApiResponseDTO<List<ScheduleResponseDTO>> getSchedulesByRange(
            @RequestParam(value = "start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime start,
            @RequestParam(value = "end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime end,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ApiResponseDTO.success(scheduleService.findSchedulesByRange(userDetails.getUserId(), start, end));
    }

    @GetMapping("/tags/{tagId}/schedules")
    public ApiResponseDTO<List<ScheduleResponseDTO>> getSchedulesByTagId(
            @PathVariable("tagId") Long tagId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ApiResponseDTO.success(scheduleService.findSchedulesByTagId(userDetails.getUserId(), tagId));
    }

    @PutMapping("/schedules/{scheduleId}")
    public ApiResponseDTO<ScheduleResponseDTO> updateSchedule(
            @PathVariable("scheduleId") Long scheduleId,
            @RequestBody ScheduleUpdateDTO scheduleUpdateDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ApiResponseDTO.success(scheduleService.updateSchedule(userDetails.getUserId(), scheduleId, scheduleUpdateDTO));
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public ApiResponseDTO<String> deleteSchedule(
            @PathVariable("scheduleId") Long scheduleId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ApiResponseDTO.success(scheduleService.deleteSchedule(userDetails.getUserId(), scheduleId));
    }
}

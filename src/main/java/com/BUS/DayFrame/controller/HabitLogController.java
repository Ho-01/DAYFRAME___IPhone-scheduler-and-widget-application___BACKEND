package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.response.ApiResponseDTO;
import com.BUS.DayFrame.dto.response.CompleteHabitResponseDTO;
import com.BUS.DayFrame.dto.response.HabitLogResponseDTO;
import com.BUS.DayFrame.service.HabitLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habitlogs")
@RequiredArgsConstructor
public class HabitLogController {

    private final HabitLogService habitLogService;


    @PostMapping("/{habitId}/complete")
    public ResponseEntity<ApiResponseDTO<CompleteHabitResponseDTO>> completeHabit(
            @PathVariable Long habitId,
            @AuthenticationPrincipal UserDetails userDetails) {

        CompleteHabitResponseDTO response = habitLogService.completeHabit(habitId, userDetails);
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }


    @DeleteMapping("/{habitLogId}")
    public ResponseEntity<ApiResponseDTO<String>> cancelHabitCompletion(
            @PathVariable Long habitLogId,
            @AuthenticationPrincipal UserDetails userDetails) {

        habitLogService.cancelHabitCompletion(habitLogId, userDetails);
        return ResponseEntity.ok(ApiResponseDTO.success("습관 완료 취소 성공"));
    }

    @GetMapping("/mine")
    public ResponseEntity<ApiResponseDTO<List<HabitLogResponseDTO>>> getCompletedLogs(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<HabitLogResponseDTO> response = habitLogService.getCompletedHabits(userDetails);
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

}

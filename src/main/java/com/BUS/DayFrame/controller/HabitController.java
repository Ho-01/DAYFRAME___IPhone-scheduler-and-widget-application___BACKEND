package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.HabitCreateDTO;
import com.BUS.DayFrame.dto.request.HabitUpdateDTO;
import com.BUS.DayFrame.dto.response.ApiResponseDTO;
import com.BUS.DayFrame.dto.response.HabitResponseDTO;
import com.BUS.DayFrame.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;


    @PostMapping
    public ResponseEntity<ApiResponseDTO<HabitResponseDTO>> createHabit(
            @RequestBody HabitCreateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        HabitResponseDTO response = habitService.createHabit(dto, userDetails);
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }


    @GetMapping("/mine")
    public ResponseEntity<ApiResponseDTO<List<HabitResponseDTO>>> getHabits(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<HabitResponseDTO> response = habitService.getHabits(userDetails);
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }


    @PutMapping("/{habitId}")
    public ResponseEntity<ApiResponseDTO<HabitResponseDTO>> updateHabit(
            @PathVariable Long habitId,
            @RequestBody HabitUpdateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        HabitResponseDTO response = habitService.updateHabit(habitId, dto, userDetails);
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }


    @DeleteMapping("/{habitId}")
    public ResponseEntity<ApiResponseDTO<String>> deleteHabit(
            @PathVariable Long habitId,
            @AuthenticationPrincipal UserDetails userDetails) {

        habitService.deleteHabit(habitId, userDetails);
        return ResponseEntity.ok(ApiResponseDTO.success("습관 삭제 성공"));
    }
}

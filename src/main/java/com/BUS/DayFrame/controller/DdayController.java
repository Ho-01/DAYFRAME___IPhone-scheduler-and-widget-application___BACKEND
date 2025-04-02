package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.DdayCreateDTO;
import com.BUS.DayFrame.dto.request.DdayUpdateDTO;
import com.BUS.DayFrame.dto.response.ApiResponseDTO;
import com.BUS.DayFrame.dto.response.DdayResponseDTO;
import com.BUS.DayFrame.service.DdayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ddays")
@RequiredArgsConstructor
public class DdayController {

    private final DdayService ddayService;


    @PostMapping
    public ResponseEntity<ApiResponseDTO<DdayResponseDTO>> createDday(
            @RequestBody DdayCreateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        DdayResponseDTO response = ddayService.createDday(dto, userDetails);
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }


    @GetMapping("/mine")
    public ResponseEntity<ApiResponseDTO<List<DdayResponseDTO>>> getDdays(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<DdayResponseDTO> response = ddayService.getDdays(userDetails);
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }


    @PutMapping("/{ddayId}")
    public ResponseEntity<ApiResponseDTO<DdayResponseDTO>> updateDday(
            @PathVariable Long ddayId,
            @RequestBody DdayUpdateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        DdayResponseDTO response = ddayService.updateDday(ddayId, dto, userDetails);
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }


    @DeleteMapping("/{ddayId}")
    public ResponseEntity<ApiResponseDTO<String>> deleteDday(
            @PathVariable Long ddayId,
            @AuthenticationPrincipal UserDetails userDetails) {

        ddayService.deleteDday(ddayId, userDetails);
        return ResponseEntity.ok(ApiResponseDTO.success("디데이 삭제 성공"));
    }
}

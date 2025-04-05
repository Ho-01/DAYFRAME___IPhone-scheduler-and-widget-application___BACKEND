package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.WidgetCreateDTO;
import com.BUS.DayFrame.dto.request.WidgetUpdateDTO;
import com.BUS.DayFrame.dto.response.ApiResponseDTO;
import com.BUS.DayFrame.dto.response.WidgetResponseDTO;
import com.BUS.DayFrame.security.service.CustomUserDetails;
import com.BUS.DayFrame.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WidgetController {
    @Autowired
    private WidgetService widgetService;

    @PostMapping("/widget-boards/{boardId}/widgets")
    public ApiResponseDTO<WidgetResponseDTO> createWidget(
            @PathVariable("boardId") Long boardId,
            @RequestBody WidgetCreateDTO widgetCreateDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ApiResponseDTO.success(widgetService.saveWidget(userDetails.getUserId(), boardId, widgetCreateDTO));
    }

    @GetMapping("/widget-boards/{boardId}/widgets")
    public ApiResponseDTO<List<WidgetResponseDTO>> getWidgetsByBoardId(
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ApiResponseDTO.success(widgetService.findWidgetsByBoardId(userDetails.getUserId(), boardId));
    }

    @PutMapping("/widgets/{widgetId}")
    public ApiResponseDTO<WidgetResponseDTO> updateWidget(
            @PathVariable("widgetId") Long widgetId,
            @RequestBody WidgetUpdateDTO widgetUpdateDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ApiResponseDTO.success(widgetService.updateWidget(userDetails.getUserId(), widgetId, widgetUpdateDTO));
    }

    @DeleteMapping("/widgets/{widgetId}")
    public ApiResponseDTO<?> deleteWidget(
            @PathVariable("widgetId") Long widgetId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        widgetService.deleteWidget(userDetails.getUserId(), widgetId);
        return ApiResponseDTO.success("위젯 삭제 성공");
    }
}
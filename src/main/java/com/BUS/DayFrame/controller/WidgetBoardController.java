package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.WidgetBoardCreateDTO;
import com.BUS.DayFrame.dto.request.WidgetBoardUpdateDTO;
import com.BUS.DayFrame.dto.response.ApiResponseDTO;
import com.BUS.DayFrame.dto.response.WidgetBoardResponseDTO;
import com.BUS.DayFrame.security.service.CustomUserDetails;
import com.BUS.DayFrame.service.WidgetBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WidgetBoardController {
    @Autowired
    private WidgetBoardService widgetBoardService;

    @PostMapping("/widget-boards")
    public ApiResponseDTO<WidgetBoardResponseDTO> createWidgetBoard(
            @RequestBody WidgetBoardCreateDTO widgetBoardCreateDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        WidgetBoardResponseDTO widgetBoardResponseDTO = widgetBoardService.saveWidgetBoard(userDetails.getUserId(), widgetBoardCreateDTO);
        return ApiResponseDTO.success(widgetBoardResponseDTO);
    }

    @GetMapping("/widget-boards/mine")
    public ApiResponseDTO<List<WidgetBoardResponseDTO>> getMyWidgetBoards(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        List<WidgetBoardResponseDTO> widgetBoardResponseDTOList = widgetBoardService.findWidgetBoardsByUserId(userDetails.getUserId());
        return ApiResponseDTO.success(widgetBoardResponseDTOList);
    }

    @PutMapping("/widget-boards/{boardId}")
    public ApiResponseDTO<WidgetBoardResponseDTO> updateWidgetBoard(
            @PathVariable("boardId") Long boardId,
            @RequestBody WidgetBoardUpdateDTO widgetBoardUpdateDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        WidgetBoardResponseDTO widgetBoardResponseDTO = widgetBoardService.updateWidgetBoard(userDetails.getUserId(), boardId, widgetBoardUpdateDTO);
        return ApiResponseDTO.success(widgetBoardResponseDTO);
    }

    @DeleteMapping("/widget-boards/{boardId}")
    public ApiResponseDTO<?> deleteWidgetBoard(
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        widgetBoardService.deleteWidgetBoard(userDetails.getUserId(), boardId);
        return ApiResponseDTO.success("위젯보드 삭제 성공");
    }
}

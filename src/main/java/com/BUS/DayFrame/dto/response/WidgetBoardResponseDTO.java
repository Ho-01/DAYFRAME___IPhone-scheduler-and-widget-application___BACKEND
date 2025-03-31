package com.BUS.DayFrame.dto.response;

import com.BUS.DayFrame.domain.BoardType;
import com.BUS.DayFrame.domain.WidgetBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class WidgetBoardResponseDTO {
    private Long id;
    private BoardType boardType;
    private String boardName;

    public static WidgetBoardResponseDTO fromEntity(WidgetBoard widgetBoard){
        return new WidgetBoardResponseDTO(
                widgetBoard.getId(),
                widgetBoard.getBoardType(),
                widgetBoard.getBoardName()
        );
    }

    public static List<WidgetBoardResponseDTO> fromEntityList(List<WidgetBoard> widgetBoardList){
        List<WidgetBoardResponseDTO> widgetBoardResponseDTOList = new ArrayList<>();
        for(WidgetBoard widgetBoard : widgetBoardList){
            widgetBoardResponseDTOList.add(WidgetBoardResponseDTO.fromEntity(widgetBoard));
        }
        return widgetBoardResponseDTOList;
    }
}

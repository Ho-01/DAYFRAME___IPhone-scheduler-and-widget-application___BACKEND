package com.BUS.DayFrame.dto.request;

import com.BUS.DayFrame.domain.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WidgetBoardCreateDTO {
    private BoardType boardType;
    private String boardName;
}

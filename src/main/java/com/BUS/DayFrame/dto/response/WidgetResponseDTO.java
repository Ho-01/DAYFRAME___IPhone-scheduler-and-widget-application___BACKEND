package com.BUS.DayFrame.dto.response;

import com.BUS.DayFrame.domain.Widget;
import com.BUS.DayFrame.domain.WidgetType;
import com.BUS.DayFrame.domain.styleconfig.StyleConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class WidgetResponseDTO {
    private Long id;
    private WidgetType widgetType;
    private int x;
    private int y;
    private int width;
    private int height;
    private StyleConfig styleConfig;

    public static WidgetResponseDTO fromEntity(Widget widget){
        return new WidgetResponseDTO(
                widget.getId(),
                widget.getWidgetType(),
                widget.getX(),
                widget.getY(),
                widget.getWidth(),
                widget.getHeight(),
                widget.getStyleConfig()
        );
    }

    public static List<WidgetResponseDTO> fromEntityList(List<Widget> widgetList){
        List<WidgetResponseDTO> widgetResponseDTOList = new ArrayList<>();
        for(Widget widget : widgetList){
            widgetResponseDTOList.add(WidgetResponseDTO.fromEntity(widget));
        }
        return widgetResponseDTOList;
    }
}

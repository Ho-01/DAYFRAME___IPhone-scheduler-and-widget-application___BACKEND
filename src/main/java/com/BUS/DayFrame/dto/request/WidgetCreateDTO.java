package com.BUS.DayFrame.dto.request;

import com.BUS.DayFrame.domain.WidgetType;
import com.BUS.DayFrame.domain.styleconfig.StyleConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class WidgetCreateDTO {
    private WidgetType widgetType;
    private int x;
    private int y;
    private int width;
    private int height;
    private StyleConfig styleConfig;
}

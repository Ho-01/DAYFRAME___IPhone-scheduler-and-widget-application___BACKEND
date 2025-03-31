package com.BUS.DayFrame.dto.request;

import com.BUS.DayFrame.domain.styleconfig.StyleConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WidgetUpdateDTO {
    private int x;
    private int y;
    private int width;
    private int height;
    private StyleConfig styleConfig;
}

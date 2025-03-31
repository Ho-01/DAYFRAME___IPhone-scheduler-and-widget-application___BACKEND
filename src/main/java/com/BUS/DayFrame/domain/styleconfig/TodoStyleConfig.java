package com.BUS.DayFrame.domain.styleconfig;

import com.BUS.DayFrame.domain.WidgetType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Time;
import java.util.Set;

public class TodoStyleConfig implements StyleConfig{
    @JsonProperty("widgetType")
    private WidgetType widgetType;
    @JsonProperty("displayRange")
    private String displayRange;
    @JsonProperty("customStart")
    private Time customStart;
    @JsonProperty("customEnd")
    private Time customEnd;
    @JsonProperty("font")
    private String font;
    @JsonProperty("fontSize")
    private String fontSize;
    @JsonProperty("fontColor")
    private String fontColor;
    @JsonProperty("backgroundColor")
    private String backgroundColor;

    @Override
    public void validate() {
        if (!Set.of("custom", "24h").contains(displayRange)) {
            throw new IllegalArgumentException("displayRange 값은 'custom' 또는 '24h' 여야 합니다.");
        }
        if (displayRange.equals("custom")) {
            if (customStart == null || customEnd == null) {
                throw new IllegalArgumentException("custom 설정 시 customStart와 customEnd는 필수입니다.");
            }
        }
    }
}

package com.BUS.DayFrame.domain.styleconfig;

import com.BUS.DayFrame.domain.WidgetType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MemoStyleConfig implements StyleConfig{
    @JsonProperty("widgetType")
    private WidgetType widgetType;
    @JsonProperty("content")
    private String content;
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

    }
}

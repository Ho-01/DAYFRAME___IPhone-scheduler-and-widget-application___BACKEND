package com.BUS.DayFrame.domain.styleconfig;

import com.BUS.DayFrame.domain.WidgetType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class DdayStyleConfig implements StyleConfig{
    @JsonProperty("widgetType")
    private WidgetType widgetType;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("targetDate")
    private Date targetDate;
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

package com.BUS.DayFrame.domain.styleconfig;

import com.BUS.DayFrame.domain.WidgetType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Time;

public class HabitStyleConfig implements StyleConfig{
    @JsonProperty("widgetType")
    private WidgetType widgetType;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("habitTitle")
    private String habitTitle;
    @JsonProperty("showAchievementChart")
    private Boolean showAchievementChart;
    @JsonProperty("achievementChartType")
    private String achievementChartType;
    @JsonProperty("chartColor")
    private String chartColor;
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

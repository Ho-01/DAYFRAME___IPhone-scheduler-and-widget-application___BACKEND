package com.BUS.DayFrame.domain.styleconfig;

import com.BUS.DayFrame.domain.WidgetType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PhotoStyleConfig implements StyleConfig{
    @JsonProperty("widgetType")
    private WidgetType widgetType;
    @JsonProperty("photoUrl")
    private String photoUrl;
    @JsonProperty("fillType")
    private String fillType;

    @Override
    public void validate() {

    }
}

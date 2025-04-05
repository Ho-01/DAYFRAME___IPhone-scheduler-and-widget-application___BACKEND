package com.BUS.DayFrame.domain.styleconfig;

import com.BUS.DayFrame.domain.WidgetType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Time;

public class QuoteStyleConfig implements StyleConfig{
    @JsonProperty("widgetType")
    private WidgetType widgetType;
    @JsonProperty("author")
    private String author;
    @JsonProperty("authorProfile")
    private String authorProfile;
    @JsonProperty("quote")
    private String quote;
    @JsonProperty("showAuthor")
    private Boolean showAuthor;
    @JsonProperty("showAuthorProfile")
    private Boolean showAuthorProfile;
    @JsonProperty("autoRefresh")
    private Boolean autoRefresh;
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

package com.BUS.DayFrame.domain.styleconfig;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "widgetType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MemoStyleConfig.class, name = "MEMO"),
        @JsonSubTypes.Type(value = PhotoStyleConfig.class, name = "PHOTO"),
        @JsonSubTypes.Type(value = DdayStyleConfig.class, name = "DDAY"),
        @JsonSubTypes.Type(value = TodoStyleConfig.class, name = "TODO"),
        @JsonSubTypes.Type(value = HabitStyleConfig.class, name = "HABIT"),
        @JsonSubTypes.Type(value = QuoteStyleConfig.class, name = "QUOTE"),
        @JsonSubTypes.Type(value = TimerStyleConfig.class, name = "TIMER")
})
public interface StyleConfig {
    void validate();
}

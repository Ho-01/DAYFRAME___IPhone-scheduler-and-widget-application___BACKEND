package com.BUS.DayFrame.domain;

import com.BUS.DayFrame.domain.BoardType;
import com.BUS.DayFrame.domain.styleconfig.StyleConfig;
import com.BUS.DayFrame.domain.styleconfig.StyleConfigConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Widget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WidgetType widgetType;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    @Column(nullable = false)
    private int width;

    @Column(nullable = false)
    private int height;

    @Column(columnDefinition = "text", nullable = false)
//    이 부분은 MySQL로 DB 바꿨을때 적용할것
//    @Column(columnDefinition = "json", nullable = false)
    @Convert(converter = StyleConfigConverter.class)
    private StyleConfig styleConfig;

    @Builder
    public Widget(Long boardId, WidgetType widgetType, int x, int y, int width, int height, StyleConfig styleConfig){
        this.boardId = boardId;
        this.widgetType = widgetType;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.styleConfig = styleConfig;
    }

    public void updateWidget(int x, int y, int width, int height, StyleConfig styleConfig) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.styleConfig = styleConfig;
    }
}

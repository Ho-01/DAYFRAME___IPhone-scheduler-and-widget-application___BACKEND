package com.BUS.DayFrame.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WidgetBoard {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Column(nullable = false)
    private String boardName;

    @Builder
    public WidgetBoard(Long userId, BoardType boardType, String boardName){
        this.userId = userId;
        this.boardType = boardType;
        this.boardName = boardName;
    }

    public void updateWidgetBoard(String boardName){
        this.boardName = boardName;
    }
}

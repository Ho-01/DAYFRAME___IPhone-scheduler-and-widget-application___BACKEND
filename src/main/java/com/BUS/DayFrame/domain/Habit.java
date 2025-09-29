package com.BUS.DayFrame.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String habitTitle;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = true)
    private LocalDate endDate;

    @Column(nullable = false, length = 50)
    private String repeatDays;

    @Builder
    public Habit(User user, String habitTitle, LocalDate startDate, LocalDate endDate, String repeatDays) {
        this.user = user;
        this.habitTitle = habitTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.repeatDays = repeatDays;
    }

    public void update(String habitTitle, LocalDate startDate, LocalDate endDate, String repeatDays) {
        this.habitTitle = habitTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.repeatDays = repeatDays;
    }
}

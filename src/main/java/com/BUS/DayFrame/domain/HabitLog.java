package com.BUS.DayFrame.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HabitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "habit_id")
    private Habit habit;

    @Column(nullable = false)
    private LocalDate targetDate;

    private LocalDateTime completedAt;

    private Boolean success;
}

package com.BUS.DayFrame.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Dday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate targetDate;

    @Builder
    public Dday(User user, String title, LocalDate targetDate) {
        this.user = user;
        this.title = title;
        this.targetDate = targetDate;
    }

    public void update(String title, LocalDate targetDate) {
        this.title = title;
        this.targetDate = targetDate;
    }
}


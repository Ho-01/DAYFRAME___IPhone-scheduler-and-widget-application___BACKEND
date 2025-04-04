package com.BUS.DayFrame.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private Boolean allDay;

    private String location;

    private String description;

    @Column(nullable = false)
    private Boolean isDone;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ScheduleTag> scheduleTags = new ArrayList<>();

    @Builder
    public Schedule(User user, String name, LocalDateTime startDateTime, LocalDateTime endDateTime, Boolean allDay, String location, String description){
        this.user = user;
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.allDay = allDay;
        this.location = location;
        this.description = description;
    }

    public void updateSchedule(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, Boolean allDay, String location, String description, Boolean isDone){
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.allDay = allDay;
        this.location = location;
        this.description = description;
        this.isDone = isDone;
    }
}

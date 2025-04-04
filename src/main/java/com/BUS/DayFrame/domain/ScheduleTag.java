package com.BUS.DayFrame.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleTag {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Builder
    public ScheduleTag(Schedule schedule, Tag tag){
        this.schedule = schedule;
        this.tag = tag;
    }

    public void createRelationship(Schedule schedule, Tag tag){
        this.schedule = schedule;
        this.tag = tag;
        schedule.getScheduleTags().add(this);
    }

    public void removeRelationship(){
        if(this.schedule != null){
            this.schedule.getScheduleTags().remove(this);
            this.schedule = null;
        }
    }
}

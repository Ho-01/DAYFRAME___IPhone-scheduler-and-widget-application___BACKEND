package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.Schedule;
import com.BUS.DayFrame.domain.ScheduleTag;
import com.BUS.DayFrame.domain.Tag;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.request.ScheduleCreateDTO;
import com.BUS.DayFrame.dto.request.ScheduleUpdateDTO;
import com.BUS.DayFrame.dto.response.ScheduleResponseDTO;
import com.BUS.DayFrame.repository.ScheduleJpaRepository;
import com.BUS.DayFrame.repository.ScheduleTagJpaRepository;
import com.BUS.DayFrame.repository.TagJpaRepository;
import com.BUS.DayFrame.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleJpaRepository scheduleJpaRepository;
    @Autowired
    private TagJpaRepository tagJpaRepository;
    @Autowired
    private ScheduleTagJpaRepository scheduleTagJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Transactional
    public ScheduleResponseDTO saveSchedule(Long userId, ScheduleCreateDTO scheduleCreateDTO) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("id: "+userId+" 에 해당하는 user를 잧을 수 없음."));
        Schedule schedule = newScheduleFromDTO(scheduleCreateDTO, user);
        scheduleJpaRepository.save(schedule);
        user.getSchedules().add(schedule);
        for(Long tagId: scheduleCreateDTO.getTagIds()){
            Tag tag = tagJpaRepository.findById(tagId)
                    .orElseThrow(() -> new EntityNotFoundException("id: "+tagId+" 에 해당하는 tag를 잧을 수 없음."));
            if(!userId.equals(tag.getUser().getId())){
                throw new IllegalArgumentException("id: "+tagId+"에 해당하는 tag는 사용자 소유가 아닙니다");
            }
            ScheduleTag scheduleTag = new ScheduleTag(schedule, tag);
            scheduleTag.createRelationship(schedule, tag);
            scheduleTagJpaRepository.save(scheduleTag);
        }
        return ScheduleResponseDTO.fromEntity(schedule);
    }

    private Schedule newScheduleFromDTO(ScheduleCreateDTO scheduleCreateDTO, User user) {
        LocalDateTime start = scheduleCreateDTO.getStartDateTime();
        LocalDateTime end = scheduleCreateDTO.getEndDateTime();
        Boolean allDay = scheduleCreateDTO.getAllDay();
        if (allDay) {// 하루종일 일정일 경우
            start = start.toLocalDate().atStartOfDay(); // 시작일 00:00:00
            if (end == null) {
                end = start.withHour(23).withMinute(59).withSecond(59);
            } else {
                end = end.toLocalDate().atTime(23, 59, 59); // 종료일 23:59:59
            }
        } else {// 하루종일 일정이 아니면 end가 반드시 있어야 한다
            if (end == null) {
                throw new IllegalArgumentException("종료시간은 필수입니다 (allDay=false)");
            }
        }
        return new Schedule(
                user,
                scheduleCreateDTO.getName(),
                start,
                end,
                allDay,
                scheduleCreateDTO.getLocation(),
                scheduleCreateDTO.getDescription()
        );
    }

    public List<ScheduleResponseDTO> findSchedulesByPeriod(Long userId, String period, LocalDateTime today) {
        if (period == null || today == null) {
            throw new IllegalArgumentException("period와 today는 모두 필수 입력입니다.");
        }
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        switch (period.toLowerCase()) {
            case "week":
                // 오늘 날짜가 포함된 주간(일요일~토요일) 계산
                startDateTime = today.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
                endDateTime = startDateTime.plusDays(6).withHour(23).withMinute(59).withSecond(59);
                break;
            case "month":
                // 오늘 날짜가 포함된 월간(1일~말일) 계산
                startDateTime = today.withDayOfMonth(1).toLocalDate().atStartOfDay();
                endDateTime = today.withDayOfMonth(today.toLocalDate().lengthOfMonth()).toLocalDate().atTime(23, 59, 59);
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 period 타입입니다: " + period);
        }

        List<Schedule> schedules = scheduleJpaRepository.findByUserIdAndStartDateTimeBetween(userId, startDateTime, endDateTime);
        return ScheduleResponseDTO.fromEntityList(schedules);
    }

    public List<ScheduleResponseDTO> findSchedulesByRange(Long userId, LocalDateTime start, LocalDateTime end) {
        List<Schedule> schedules = scheduleJpaRepository.findByUserIdAndStartDateTimeBetween(userId, start, end);
        return ScheduleResponseDTO.fromEntityList(schedules);
    }

    public List<ScheduleResponseDTO> findSchedulesByTagId(Long userId, Long tagId) {
        Tag tag = tagJpaRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("id: "+tagId+" 에 해당하는 tag를 잧을 수 없음."));
        if(!userId.equals(tag.getUser().getId())){
            throw new IllegalArgumentException("id: "+tagId+"에 해당하는 tag는 사용자 소유가 아닙니다");
        }
        List<ScheduleTag> scheduleTagList = scheduleTagJpaRepository.findByTag(tag);
        List<Schedule> scheduleList = new ArrayList<>();
        for(ScheduleTag scheduleTag: scheduleTagList){
            scheduleList.add(scheduleTag.getSchedule());
        }
        return ScheduleResponseDTO.fromEntityList(scheduleList);
    }

    @Transactional
    public ScheduleResponseDTO updateSchedule(Long userId, Long scheduleId, ScheduleUpdateDTO scheduleUpdateDTO) {
        Schedule schedule = scheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("id: "+scheduleId+" 에 해당하는 schedule을 잧을 수 없음."));
        if(!userId.equals(schedule.getUser().getId())){
            throw new IllegalArgumentException("id: "+scheduleId+"에 해당하는 schedule은 사용자 소유가 아닙니다");
        }
        System.out.println("서비스진입");
        schedule.updateSchedule(
                scheduleUpdateDTO.getName(),
                scheduleUpdateDTO.getStartDateTime(),
                scheduleUpdateDTO.getEndDateTime(),
                scheduleUpdateDTO.getAllDay(),
                scheduleUpdateDTO.getLocation(),
                scheduleUpdateDTO.getDescription(),
                scheduleUpdateDTO.getIsDone()
        );
        List<ScheduleTag> scheduleTagsCopy = new ArrayList<>(schedule.getScheduleTags());
        for(ScheduleTag scheduleTag : scheduleTagsCopy){
            scheduleTag.removeRelationship();
            scheduleTagJpaRepository.delete(scheduleTag);
        }
        for(Long tagId: scheduleUpdateDTO.getTagIds()){
            Tag tag = tagJpaRepository.findById(tagId)
                    .orElseThrow(() -> new EntityNotFoundException("id: "+tagId+" 에 해당하는 tag를 잧을 수 없음."));
            if(!userId.equals(tag.getUser().getId())){
                throw new IllegalArgumentException("id: "+tagId+"에 해당하는 tag는 사용자 소유가 아닙니다");
            }
            ScheduleTag scheduleTag = new ScheduleTag(schedule, tag);
            scheduleTag.createRelationship(schedule, tag);
            scheduleTagJpaRepository.save(scheduleTag);
        }
        return ScheduleResponseDTO.fromEntity(schedule);
    }

    @Transactional
    public String deleteSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("id: "+scheduleId+" 에 해당하는 schedule을 잧을 수 없음."));
        if(!userId.equals(schedule.getUser().getId())){
            throw new IllegalArgumentException("id: "+scheduleId+"에 해당하는 schedule은 사용자 소유가 아닙니다");
        }
        List<ScheduleTag> scheduleTagsCopy = new ArrayList<>(schedule.getScheduleTags());
        for(ScheduleTag scheduleTag: scheduleTagsCopy){
            scheduleTag.removeRelationship();
            scheduleTagJpaRepository.delete(scheduleTag);
        }
        scheduleJpaRepository.delete(schedule);
        return "일정 삭제 완료";
    }
}

package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.Habit;
import com.BUS.DayFrame.domain.HabitLog;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.response.CompleteHabitResponseDTO;
import com.BUS.DayFrame.dto.response.HabitLogResponseDTO;
import com.BUS.DayFrame.repository.HabitJpaRepository;
import com.BUS.DayFrame.repository.HabitLogJpaRepository;
import com.BUS.DayFrame.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitLogService {

    private final HabitJpaRepository habitRepository;
    private final HabitLogJpaRepository habitLogRepository;
    private final UserJpaRepository userRepository;

    @Transactional
    public CompleteHabitResponseDTO completeHabit(Long habitId, UserDetails userDetails) {
        User user = getUser(userDetails);

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new EntityNotFoundException("해당 습관이 존재하지 않습니다."));

        if (!habit.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 습관만 완료할 수 있습니다.");
        }

        HabitLog log = HabitLog.builder()
                .habit(habit)
                .targetDate(LocalDate.now())
                .completedAt(LocalDateTime.now())
                .success(true)
                .build();

        habitLogRepository.save(log);

        return new CompleteHabitResponseDTO(habit.getId(), log.getCompletedAt());
    }



    @Transactional
    public void cancelHabitCompletion(Long habitLogId, UserDetails userDetails) {
        HabitLog log = habitLogRepository.findById(habitLogId)
                .orElseThrow(() -> new EntityNotFoundException("해당 HabitLog가 존재하지 않습니다."));

        if (!log.getHabit().getUser().getEmail().equals(userDetails.getUsername())) {
            throw new IllegalArgumentException("본인의 완료 기록만 삭제할 수 있습니다.");
        }

        habitLogRepository.delete(log);
    }

    private User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<HabitLogResponseDTO> getCompletedHabits(UserDetails userDetails) {
        User user = getUser(userDetails);

        return habitLogRepository.findByHabit_User(user).stream()
                .map(log -> new HabitLogResponseDTO(
                        log.getId(),
                        log.getHabit().getId(),
                        log.getHabit().getHabitTitle(),
                        log.getTargetDate(),
                        log.getCompletedAt(),
                        log.getSuccess()
                ))
                .toList();
    }

}

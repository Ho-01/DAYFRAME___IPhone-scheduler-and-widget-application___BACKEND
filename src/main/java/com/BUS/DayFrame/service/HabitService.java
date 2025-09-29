package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.Habit;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.request.HabitCreateDTO;
import com.BUS.DayFrame.dto.request.HabitUpdateDTO;
import com.BUS.DayFrame.dto.response.HabitResponseDTO;
import com.BUS.DayFrame.repository.HabitJpaRepository;
import com.BUS.DayFrame.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitJpaRepository habitRepository;
    private final UserJpaRepository userRepository;

    @Transactional
    public HabitResponseDTO createHabit(HabitCreateDTO dto, UserDetails userDetails) {
        validateRepeatDays(dto.getRepeatDays());

        User user = getUserFrom(userDetails);

        Habit habit = Habit.builder()
                .user(user)
                .habitTitle(dto.getHabitTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .repeatDays(dto.getRepeatDays())
                .build();

        return toResponse(habitRepository.save(habit));
    }

    @Transactional(readOnly = true)
    public List<HabitResponseDTO> getHabits(UserDetails userDetails) {
        User user = getUserFrom(userDetails);
        return habitRepository.findByUser(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public HabitResponseDTO updateHabit(Long habitId, HabitUpdateDTO dto, UserDetails userDetails) {
        validateRepeatDays(dto.getRepeatDays());

        Habit habit = getOwnedHabitOrThrow(habitId, userDetails);

        habit.update(dto.getHabitTitle(), dto.getStartDate(), dto.getEndDate(), dto.getRepeatDays());

        return toResponse(habit);
    }



    @Transactional
    public void deleteHabit(Long habitId, UserDetails userDetails) {
        Habit habit = getOwnedHabitOrThrow(habitId, userDetails);
        habitRepository.delete(habit);
    }


    private User getUserFrom(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
    }

    //본인 소유의 habits인지 확인(보안상 필요)
    private Habit getOwnedHabitOrThrow(Long habitId, UserDetails userDetails) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new EntityNotFoundException("해당 습관이 존재하지 않습니다."));

        if (!habit.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new IllegalArgumentException("본인의 습관만 수정/삭제할 수 있습니다.");
        }

        return habit;
    }


    private HabitResponseDTO toResponse(Habit habit) {
        return new HabitResponseDTO(
                habit.getId(),
                habit.getHabitTitle(),
                habit.getStartDate(),
                habit.getEndDate(),
                habit.getRepeatDays()
        );
    }
 // 뭔가 개선이 필요할거같은 느낌적인 느낌느김
    private static final Set<String> VALID_DAYS = Set.of(
            "sun", "mon", "tue", "wed", "thu", "fri", "sat"
    );

    private void validateRepeatDays(String repeatDays) {
        boolean allValid = Arrays.stream(repeatDays.split(","))
                .map(String::trim)
                .allMatch(day -> VALID_DAYS.contains(day.toLowerCase()));

        if (!allValid) {
            throw new IllegalArgumentException("반복 요일은 sun, mon, tue, wed, thu, fri, sat 중에서 선택해야 합니다.");
        }
    }

}

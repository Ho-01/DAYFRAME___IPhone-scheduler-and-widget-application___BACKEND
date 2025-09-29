package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.Dday;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.request.DdayCreateDTO;
import com.BUS.DayFrame.dto.request.DdayUpdateDTO;
import com.BUS.DayFrame.dto.response.DdayResponseDTO;
import com.BUS.DayFrame.repository.DdayJpaRepository;
import com.BUS.DayFrame.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DdayService {

    private final DdayJpaRepository ddayRepository;
    private final UserJpaRepository userRepository;

    @Transactional
    public DdayResponseDTO createDday(DdayCreateDTO dto, UserDetails userDetails) {
        User user = getUserFrom(userDetails);

        Dday dday = Dday.builder()
                .user(user)
                .title(dto.getTitle())
                .targetDate(dto.getTargetDate())
                .build();

        return toResponse(ddayRepository.save(dday));
    }

    @Transactional(readOnly = true)
    public List<DdayResponseDTO> getDdays(UserDetails userDetails) {
        User user = getUserFrom(userDetails);
        return ddayRepository.findByUser(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DdayResponseDTO updateDday(Long ddayId, DdayUpdateDTO dto, UserDetails userDetails) {
        Dday dday = getOwnedDdayOrThrow(ddayId, userDetails);
        dday.update(dto.getTitle(), dto.getTargetDate());
        return toResponse(dday);
    }

    @Transactional
    public void deleteDday(Long ddayId, UserDetails userDetails) {
        Dday dday = getOwnedDdayOrThrow(ddayId, userDetails);
        ddayRepository.delete(dday);
    }

    // 유저 인증 관련
    private User getUserFrom(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
    }

    private Dday getOwnedDdayOrThrow(Long ddayId, UserDetails userDetails) {
        Dday dday = ddayRepository.findById(ddayId)
                .orElseThrow(() -> new EntityNotFoundException("해당 디데이가 존재하지 않습니다."));

        if (!dday.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new IllegalArgumentException("본인의 디데이만 수정/삭제할 수 있습니다.");
        }

        return dday;
    }

    private DdayResponseDTO toResponse(Dday dday) {
        return new DdayResponseDTO(
                dday.getId(),
                dday.getTitle(),
                dday.getTargetDate()
        );
    }
}

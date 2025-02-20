package com.BUS.DayFrame.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserResponseDTO {
    private final Long id;
    private final String email;
    private final String name;
    private final LocalDateTime createdAt;
}

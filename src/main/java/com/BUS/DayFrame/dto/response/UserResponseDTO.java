package com.BUS.DayFrame.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponseDTO {
    private String email;
    private String name;
    private LocalDateTime createdAt;
    private String authProvider;
}

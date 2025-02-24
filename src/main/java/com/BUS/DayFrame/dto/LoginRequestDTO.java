package com.BUS.DayFrame.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequestDTO {
    private final String email;
    private final String password;
}

package com.BUS.DayFrame.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserRegisterDTO {
    private final String email;
    private final String password;
    private final String name;

}

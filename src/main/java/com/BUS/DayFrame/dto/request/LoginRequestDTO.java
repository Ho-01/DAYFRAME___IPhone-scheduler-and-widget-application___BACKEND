package com.BUS.DayFrame.dto.request;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class LoginRequestDTO {
    private String email;
    private String password;
}

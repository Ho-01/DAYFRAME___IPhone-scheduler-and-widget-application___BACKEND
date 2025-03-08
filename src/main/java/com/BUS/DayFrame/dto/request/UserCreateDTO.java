package com.BUS.DayFrame.dto.request;


import lombok.Getter;

@Getter
public class UserCreateDTO {
    private String email;
    private String password;
    private String name;
}

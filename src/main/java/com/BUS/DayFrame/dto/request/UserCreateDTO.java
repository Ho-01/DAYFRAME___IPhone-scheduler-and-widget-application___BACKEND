package com.BUS.DayFrame.dto.request;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class UserCreateDTO {
    private  String email;
    private  String password;
    private  String name;
    private  String authProvider;


}

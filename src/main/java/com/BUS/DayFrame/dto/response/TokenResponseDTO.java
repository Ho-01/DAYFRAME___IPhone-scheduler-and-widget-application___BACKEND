package com.BUS.DayFrame.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDTO {
    private boolean success;
    private String accessToken;
    private String refreshToken;

}

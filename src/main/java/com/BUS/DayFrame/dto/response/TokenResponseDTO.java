package com.BUS.DayFrame.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenResponseDTO {
    private final String AccessToken;
    private final String RefreshToken;
}


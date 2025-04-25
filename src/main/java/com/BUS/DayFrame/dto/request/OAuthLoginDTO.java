package com.BUS.DayFrame.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

    @Getter
@AllArgsConstructor
public class OAuthLoginDTO {
    private String idToken; // 구글: id_token, 카카오: access_token (약간 차이 있음)
}


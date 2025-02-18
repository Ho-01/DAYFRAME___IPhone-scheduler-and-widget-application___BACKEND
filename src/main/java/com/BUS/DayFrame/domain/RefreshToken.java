package com.BUS.DayFrame.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue
    private Long id;

    private String userId;
    private String refreshToken;
    private LocalDateTime expirationTime;

    @Builder
    public RefreshToken(String userId, String refreshToken, LocalDateTime expirationTime) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
    }
}
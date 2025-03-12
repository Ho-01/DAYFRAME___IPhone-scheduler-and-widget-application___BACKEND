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

    private String email;
    private String refreshToken;
    private LocalDateTime expirationTime;

    @Builder
    public RefreshToken(String email, String refreshToken, LocalDateTime expirationTime) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
    }
}
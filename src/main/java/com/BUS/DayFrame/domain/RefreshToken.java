package com.BUS.DayFrame.domain;

import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    private String refreshToken;
    private LocalDateTime expirationTime;

    @Builder
    public RefreshToken(User user, String refreshToken, LocalDateTime expirationTime) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
    }
}
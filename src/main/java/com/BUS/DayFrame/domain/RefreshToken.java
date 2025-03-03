package com.BUS.DayFrame.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RefreshToken")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "refresh_token", length = 255)
    private String refreshToken;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    public RefreshToken() {}

    public RefreshToken(User user, String refreshToken, LocalDateTime expirationTime) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}

package com.BUS.DayFrame.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RefreshToken")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)  // delete user -> delete refresh token 용으로 만들어둠
    private User user;

    @Column(name = "refresh_token", length = 255, nullable = false)
    private String refreshToken;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    public RefreshToken(User user, String refreshToken, LocalDateTime expirationTime) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
    }

}

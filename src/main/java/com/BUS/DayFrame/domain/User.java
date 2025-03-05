package com.BUS.DayFrame.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void updateUser(String password, String name, PasswordEncoder passwordEncoder) {
        if (password != null && !password.isEmpty()) {
            this.password = passwordEncoder.encode(password);
        }
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }



}

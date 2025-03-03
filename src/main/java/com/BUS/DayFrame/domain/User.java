package com.BUS.DayFrame.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter //
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public User(String email, String encodedPassword, String name) {
        this.email = email;
        this.password = encodedPassword;
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

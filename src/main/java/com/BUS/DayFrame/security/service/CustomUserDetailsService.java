package com.BUS.DayFrame.security.service;

import com.BUS.DayFrame.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    User.UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getEmail());
                    builder.password(user.getPassword());
                    builder.roles("USER"); // 기본적으로 USER 역할 부여 (필요하면 변경 가능)
                    return builder.build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}

package com.BUS.DayFrame.security;

import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        User user = userJpaRepository.findByEmail(email).get();
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getId()+":"+user.getEmail())
                .password(user.getPassword())
                .build();
    }
}

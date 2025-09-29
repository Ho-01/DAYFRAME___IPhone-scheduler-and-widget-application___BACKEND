package com.BUS.DayFrame.security.service;

import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        User user = userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("email: "+email+" 에 해당하는 user를 잧을 수 없음."));
        return new CustomUserDetails(user);
    }
    public UserDetails loadUserByUserId(Long userId){
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("id: "+userId+" 에 해당하는 user를 잧을 수 없음."));
        return new CustomUserDetails(user);
    }
}
package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.UserRegisterDTO;
import com.BUS.DayFrame.dto.UserResponseDTO;
import com.BUS.DayFrame.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User register(UserRegisterDTO userRegisterDTO){
        if (userJpaRepository.findByEmail(userRegisterDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        User user = new User(userRegisterDTO.getEmail(), passwordEncoder.encode(userRegisterDTO.getPassword()), userRegisterDTO.getName());

        return userJpaRepository.save(user);
    }

    public UserResponseDTO getUserInfo(Long userId){
        User user = userJpaRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("userId: "+userId+" 에 해당하는 user를 잧을 수 없음."));
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getName(), user.getCreatedAt());
    }

    @Transactional
    public void updateUserInfo(){

    }
    @Transactional
    public void deleteUser(){

    }
}

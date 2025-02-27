package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.request.UserCreateDTO;
import com.BUS.DayFrame.dto.response.UserResponseDTO;
import com.BUS.DayFrame.dto.request.UserUpdateDTO;
import com.BUS.DayFrame.repository.RefreshTokenJpaRepository;
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
    private RefreshTokenJpaRepository refreshTokenJpaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User register(UserCreateDTO userCreateDTO){
        if (userJpaRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        User user = new User(userCreateDTO.getEmail(), passwordEncoder.encode(userCreateDTO.getPassword()), userCreateDTO.getName());

        return userJpaRepository.save(user);
    }

    public UserResponseDTO getUserInfo(Long userId){
        User user = userJpaRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("userId: "+userId+" 에 해당하는 user를 잧을 수 없음."));
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getName(), user.getCreatedAt());
    }

    @Transactional
    public UserResponseDTO updateUserInfo(Long userId, UserUpdateDTO userUpdateDTO){
        User user = userJpaRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("userId: "+userId+" 에 해당하는 user를 잧을 수 없음."));
        user.updateUserInfo(userUpdateDTO.getPassword(), userUpdateDTO.getName());
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getName(), user.getCreatedAt());
    }
    @Transactional
    public void deleteUser(Long userId){
        User user = userJpaRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("userId: "+userId+" 에 해당하는 user를 잧을 수 없음."));
        refreshTokenJpaRepository.deleteByEmail(user.getEmail());
        userJpaRepository.delete(user);
    }

    public Long getUserIdByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email))
                .getId();
    }
}

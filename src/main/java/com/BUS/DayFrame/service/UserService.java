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
    public void register(UserCreateDTO userCreateDTO){
        if (userJpaRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        User user = new User(
                userCreateDTO.getEmail(),
                passwordEncoder.encode(userCreateDTO.getPassword()),
                userCreateDTO.getName());
        userJpaRepository.save(user);
    }

    public UserResponseDTO getUserInfo(String email){
        User user = userJpaRepository.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("email: "+email+" 에 해당하는 user를 잧을 수 없음."));
        return new UserResponseDTO(user.getEmail(), user.getName(), user.getCreatedAt());
    }

    @Transactional
    public UserResponseDTO updateUserInfo(String email, UserUpdateDTO userUpdateDTO){
        User user = userJpaRepository.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("email: "+email+" 에 해당하는 user를 잧을 수 없음."));
        user.updateUserInfo(passwordEncoder.encode(userUpdateDTO.getPassword()), userUpdateDTO.getName());
        return new UserResponseDTO(user.getEmail(), user.getName(), user.getCreatedAt());
    }
    @Transactional
    public void deleteUser(String email){
        User user = userJpaRepository.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("email: "+email+" 에 해당하는 user를 잧을 수 없음."));
        refreshTokenJpaRepository.deleteByEmail(user.getEmail());
        userJpaRepository.delete(user);
    }
}
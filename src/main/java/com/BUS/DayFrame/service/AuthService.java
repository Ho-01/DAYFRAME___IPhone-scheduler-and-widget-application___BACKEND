package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.RefreshToken;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.response.TokenResponseDTO;
import com.BUS.DayFrame.repository.RefreshTokenJpaRepository;
import com.BUS.DayFrame.repository.UserJpaRepository;
import com.BUS.DayFrame.security.util.JwtTokenUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RefreshTokenJpaRepository refreshTokenJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO loginRequestDTO){
        User user = userJpaRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(()  -> new EntityNotFoundException("email: "+loginRequestDTO.getEmail()+" 에 해당하는 user를 잧을 수 없음."));
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 올바르지 않습니다.");
        }
        String accessToken = jwtTokenUtil.generateAccessToken(user.getId());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId());
        refreshTokenJpaRepository.deleteByUserId(user.getId());
        refreshTokenJpaRepository.save(new RefreshToken(
                        user.getId(),
                        refreshToken,
                        LocalDateTime.now().plusSeconds(jwtTokenUtil.REFRESH_TOKEN_EXPIRATION/1000)));
        return new TokenResponseDTO(accessToken,refreshToken);
    }

    @Transactional
    public void logout(Long userId){
        refreshTokenJpaRepository.deleteByUserId(userId);
    }

    @Transactional
    public TokenResponseDTO tokenRefresh(Long userId){
        refreshTokenJpaRepository.deleteByUserId(userId);
        String accessToken = jwtTokenUtil.generateAccessToken(userId);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userId);

        refreshTokenJpaRepository.save(new RefreshToken(
                userId,
                refreshToken,
                LocalDateTime.now().plusSeconds(jwtTokenUtil.REFRESH_TOKEN_EXPIRATION/1000)));
        return new TokenResponseDTO(accessToken, refreshToken);
    }
}
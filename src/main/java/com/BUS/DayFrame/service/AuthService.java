package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.RefreshToken;
import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.response.TokenResponseDTO;
import com.BUS.DayFrame.repository.RefreshTokenJpaRepository;
import com.BUS.DayFrame.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO loginRequestDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );
        String accessToken = jwtTokenUtil.generateAccessToken(loginRequestDTO.getEmail());
        String refreshToken = jwtTokenUtil.generateRefreshToken(loginRequestDTO.getEmail());
        refreshTokenJpaRepository.deleteByEmail(loginRequestDTO.getEmail());
        refreshTokenJpaRepository.save(new RefreshToken(
                        loginRequestDTO.getEmail(),
                        refreshToken,
                        LocalDateTime.now().plusSeconds(jwtTokenUtil.REFRESH_TOKEN_EXPIRATION/1000)));
        return new TokenResponseDTO(accessToken,refreshToken);
    }

    @Transactional
    public void logout(String email){
        refreshTokenJpaRepository.deleteByEmail(email);
    }

    @Transactional
    public TokenResponseDTO tokenRefresh(String email){
        refreshTokenJpaRepository.deleteByEmail(email);
        String accessToken = jwtTokenUtil.generateAccessToken(email);
        String refreshToken = jwtTokenUtil.generateRefreshToken(email);

        refreshTokenJpaRepository.save(new RefreshToken(
                email,
                refreshToken,
                LocalDateTime.now().plusSeconds(jwtTokenUtil.REFRESH_TOKEN_EXPIRATION/1000)));
        return new TokenResponseDTO(accessToken, refreshToken);
    }
}
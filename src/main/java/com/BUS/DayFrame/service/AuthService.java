package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.RefreshToken;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.request.LoginRequestDTO;
import com.BUS.DayFrame.dto.response.TokenResponseDTO;
import com.BUS.DayFrame.repository.RefreshTokenJpaRepository;
import com.BUS.DayFrame.repository.UserJpaRepository;
import com.BUS.DayFrame.security.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

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
        refreshTokenJpaRepository.deleteByUser(user);
        refreshTokenJpaRepository.save(new RefreshToken(
                        user,
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
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("id: "+userId+" 에 해당하는 user를 잧을 수 없음."));
        refreshTokenJpaRepository.deleteByUser(user);
        String accessToken = jwtTokenUtil.generateAccessToken(userId);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userId);

        refreshTokenJpaRepository.save(new RefreshToken(
                user,
                refreshToken,
                LocalDateTime.now().plusSeconds(jwtTokenUtil.REFRESH_TOKEN_EXPIRATION/1000)));
        return new TokenResponseDTO(accessToken, refreshToken);
    }

    public TokenResponseDTO kakaoLogin(String accessToken) {
        String email = extractEmailFromKakaoAccessToken(accessToken);
        return registerOrLogin(email, "kakao");
    }

    //소셜로그인 진입
    private TokenResponseDTO registerOrLogin(String email, String provider) {
        User user = userJpaRepository.findByEmail(email)
                .orElseGet(() -> userJpaRepository.save(
                        User.builder()
                                .email(email)
                                .password("LOGIN_WITH_" + provider.toUpperCase())   // 소셜 로그인 사용자는 비밀번호 없음
                                .name("소셜 사용자")
                                .authProvider(provider)
                                .build()
                ));

        String accessToken = jwtTokenUtil.generateAccessToken(user.getId());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId());

        refreshTokenJpaRepository.deleteByUserId(user.getId());
        refreshTokenJpaRepository.save(new RefreshToken(
                user,
                refreshToken,
                LocalDateTime.now().plusSeconds(jwtTokenUtil.REFRESH_TOKEN_EXPIRATION / 1000)
        ));

        return new TokenResponseDTO(accessToken, refreshToken);
    }




    private String extractEmailFromKakaoAccessToken(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<Void> request = new HttpEntity<>(null, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
            if (kakaoAccount == null || !Boolean.TRUE.equals(kakaoAccount.get("has_email"))) {
                throw new IllegalArgumentException("이메일 정보를 가져올 수 없습니다.");
            }

            return (String) kakaoAccount.get("email");

        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패", e);
        }
    }

    public TokenResponseDTO googleLogin(String idToken) {
        String email = extractEmailFromGoogleIdToken(idToken);
        return registerOrLogin(email, "google");
    }

    private String extractEmailFromGoogleIdToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("유효하지 않은 id_token입니다.");
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payloadMap = mapper.readValue(payload, Map.class);

            return (String) payloadMap.get("email");
        } catch (Exception e) {
            throw new RuntimeException("Google id_token 파싱 실패", e);
        }
    }


}
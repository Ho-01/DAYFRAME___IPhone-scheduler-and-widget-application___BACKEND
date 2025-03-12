package com.BUS.DayFrame.security.util;

import com.BUS.DayFrame.domain.RefreshToken;
import com.BUS.DayFrame.repository.RefreshTokenJpaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode("secretkeysecretkeysecretkeysecretkeysecretkeysecretkey"));
    public final long ACCESS_TOKEN_EXPIRATION = 1000*60*15; // 15분
    public final long REFRESH_TOKEN_EXPIRATION = 1000*60*60*24*7; // 1주일

    public String generateAccessToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+REFRESH_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    private Claims getClaims(String jwtToken){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwtToken).getPayload();
    }

    public String extractEmail(String jwtToken) {
        return getClaims(jwtToken).getSubject();
    }


    public void validateToken(String jwtToken){
        if(getClaims(jwtToken).getExpiration().before(new Date())){
            throw new ExpiredJwtException(null, null, "");
        }
    }
}
package com.BUS.DayFrame.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode("secretkeysecretkeysecretkeysecretkeysecretkeysecretkey"));
    public final long ACCESS_TOKEN_EXPIRATION = 1000*60*15; // 15분
    public final long REFRESH_TOKEN_EXPIRATION = 1000*60*60*24*7; // 1주일

    public String generateAccessToken(Long userId){
        return Jwts.builder()
                .claim("type", "access")
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Long userId){
        return Jwts.builder()
                .claim("type", "refresh")
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+REFRESH_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    private Claims getClaims(String jwtToken){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwtToken).getPayload();
    }

    public Long extractUserId(String jwtToken) {
        return Long.parseLong(getClaims(jwtToken).getSubject());
    }

    public void validateAccessToken(String jwtToken){
        String type = getClaims(jwtToken).get("type", String.class);
        if(!type.equals("access")){
            throw new IllegalArgumentException("accessToken 이 아님. 현재 타입 : "+type);
        }
        if(getClaims(jwtToken).getExpiration().before(new Date())) {
            throw new ExpiredJwtException(null, null, "");
        }
    }

    public void validateRefreshToken(String jwtToken){
        String type = getClaims(jwtToken).get("type", String.class);
        if(!type.equals("refresh")){
            throw new IllegalArgumentException("refreshToken 이 아님. 현재 타입 : "+type);
        }
        if(getClaims(jwtToken).getExpiration().before(new Date())){
            throw new ExpiredJwtException(null, null, "");
        }
    }
}
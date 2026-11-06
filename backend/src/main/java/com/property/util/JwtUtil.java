package com.property.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire-hours}")
    private long expireHours;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String create(Long userId, String username, String role) {
        Date now = new Date();
        long millis = now.getTime() + expireHours * 3600 * 1000L;
        Date expire = new Date(millis);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expire)
                .signWith(key())
                .compact();
    }

    public LoginUser parse(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        LoginUser user = new LoginUser();
        user.setUserId(Long.valueOf(claims.getSubject()));
        user.setUsername(claims.get("username", String.class));
        user.setRole(claims.get("role", String.class));
        return user;
    }
}

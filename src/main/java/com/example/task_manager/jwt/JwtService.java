package com.example.task_manager.jwt;

import com.example.task_manager.security.ApplicationConfig;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final ApplicationConfig config;
    private final SecretKey secretKey;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .issuedAt(new Date())
                .expiration(java.sql.Date.valueOf(LocalDate.now().plusDays(config.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();
    }
}

package com.example.task_manager.jwt.service;

import com.example.task_manager.jwt.exception.InvalidTokenException;
import com.example.task_manager.config.ApplicationConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final ApplicationConfig config;
    private final SecretKey secretKey;
    private final MyUserDetailsService userDetailsService;

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

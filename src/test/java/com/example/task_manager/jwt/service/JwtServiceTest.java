package com.example.task_manager.jwt.service;

import com.example.task_manager.config.ApplicationConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    @Mock private ApplicationConfig config;

    private SecretKey secretKey;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor("securedsecuredsecuredsecuredsecured".getBytes());
        when(config.getTokenExpirationAfterDays()).thenReturn(10);

        jwtService = new JwtService(config, secretKey);
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        UserDetails userDetails = new User(
                "john",
                "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtService.generateToken(userDetails);

        assertThat(token).isNotBlank();

        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getSubject()).isEqualTo("john");
        assertThat(claims.get("authorities").toString()).contains("ROLE_USER");
        assertThat(claims.getExpiration()).isAfter(new Date());
    }
}
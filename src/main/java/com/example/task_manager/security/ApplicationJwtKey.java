package com.example.task_manager.security;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class ApplicationJwtKey {
    private final ApplicationConfig config;

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(config.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }
}

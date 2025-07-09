package com.example.task_manager.security;

import com.google.common.net.HttpHeaders;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;

@ConfigurationProperties(prefix = "application.security")
@NoArgsConstructor
@Getter
@Setter
public class ApplicationConfig {
    private String adminCode;
    private SecretKey secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;

    @Bean
    public String setAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}

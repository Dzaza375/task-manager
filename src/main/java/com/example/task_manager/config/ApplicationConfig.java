package com.example.task_manager.config;

import com.google.common.net.HttpHeaders;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties(prefix = "application.security")
@NoArgsConstructor
@Getter
@Setter
public class ApplicationConfig {
    private String adminCode;
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;

    @Bean
    public String setAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}

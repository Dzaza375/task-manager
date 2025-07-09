package com.example.task_manager.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.security")
@NoArgsConstructor
@Getter
@Setter
public class ApplicationConfig {
    private String adminCode;


}

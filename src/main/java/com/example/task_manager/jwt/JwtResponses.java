package com.example.task_manager.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class JwtResponses {
    private String token;
    private LocalDateTime expiresAt;
}

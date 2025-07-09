package com.example.task_manager.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
public class JwtResponses {
    private String token;
    private LocalDateTime expiresAt;
}

package com.example.task_manager.jwt.service;

import com.example.task_manager.auth.repo.AuthRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {
    @Mock private AuthRepo authRepo;

    @InjectMocks MyUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername() {
    }
}
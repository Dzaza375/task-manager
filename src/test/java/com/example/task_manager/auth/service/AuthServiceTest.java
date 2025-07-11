package com.example.task_manager.auth.service;

import com.example.task_manager.auth.repo.AuthRepo;
import com.example.task_manager.config.ApplicationConfig;
import com.example.task_manager.user.dto.JwtRequest;
import com.example.task_manager.user.model.User;
import com.example.task_manager.user.model.UserRoles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock private AuthRepo authRepo;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ApplicationConfig config;

    @InjectMocks private AuthService authService;

    private final String TEST_USERNAME = "tom";
    private final String TEST_PASSWORD = "pass1234";
    private final String TEST_EMAIL = "tom@gmail.com";
    private final String ENCODED_PASSWORD = "encoded_pass";
    private final String CORRECT_ADMIN_CODE = "pass777";

    private JwtRequest jwtRequest;

    @BeforeEach
    void setUp() {
        jwtRequest = new JwtRequest();
        jwtRequest.setUsername(TEST_USERNAME);
        jwtRequest.setPassword(TEST_PASSWORD);
        jwtRequest.setEmail(TEST_EMAIL);

        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
    }

    @Test
    void testRegister() {
        authService.register(jwtRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(authRepo).save(captor.capture());

        User savedUser = captor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(savedUser.getPassword()).isEqualTo(ENCODED_PASSWORD);
        assertThat(savedUser.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(savedUser.getRole()).isEqualTo(UserRoles.USER);
    }

    @Test
    @Disabled
    void adminRegister() {
    }
}
package com.example.task_manager.auth.service;

import com.example.task_manager.auth.exception.IncorrectPasswordException;
import com.example.task_manager.auth.exception.UsernameAlreadyExistsException;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock private AuthRepo authRepo;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ApplicationConfig config;

    @InjectMocks private AuthService authService;

    private static final String TEST_USERNAME = "tom";
    private static final String TEST_PASSWORD = "pass1234";
    private static final String TEST_EMAIL = "tom@gmail.com";
    private static final String ENCODED_PASSWORD = "encoded_pass";
    private static final String CORRECT_ADMIN_CODE = "pass777";

    private JwtRequest jwtRequest;

    @BeforeEach
    void setUp() {
        jwtRequest = new JwtRequest();
        jwtRequest.setUsername(TEST_USERNAME);
        jwtRequest.setPassword(TEST_PASSWORD);
        jwtRequest.setEmail(TEST_EMAIL);
    }

    @Test
    void register_shouldSaveNewUser_whenUsernameDoesNotExist() {
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);

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
    void register_shouldThrowUsernameAlreadyExistsException_whenUsernameIsAlreadyTaken() {
        when(authRepo.existsByUsername(TEST_USERNAME))
                .thenReturn(true);

        assertThatThrownBy(() -> authService.register(jwtRequest))
                .isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessageContaining(TEST_USERNAME);

        verify(authRepo, never()).save(any());
    }

    @Test
    void adminRegister_shouldSaveNewAdminUser_whenAdminCodeIsCorrect() {
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(config.getAdminCode()).thenReturn(CORRECT_ADMIN_CODE);

        authService.adminRegister(jwtRequest, "pass777");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(authRepo).save(captor.capture());

        User savedAdminUser = captor.getValue();
        assertThat(savedAdminUser.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(savedAdminUser.getPassword()).isEqualTo(ENCODED_PASSWORD);
        assertThat(savedAdminUser.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(savedAdminUser.getRole()).isEqualTo(UserRoles.ADMIN);
    }

    @Test
    void adminRegister_shouldThrowIncorrectPasswordException_whenAdminCodeIsIncorrect() {
        when(config.getAdminCode()).thenReturn(CORRECT_ADMIN_CODE);

        assertThatThrownBy(() -> authService.adminRegister(jwtRequest, "incorrect code"))
                .isInstanceOf(IncorrectPasswordException.class);

        verify(authRepo, never()).save(any());
    }
}
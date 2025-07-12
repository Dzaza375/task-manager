package com.example.task_manager.admin.service;

import com.example.task_manager.admin.exception.SelfDeleteException;
import com.example.task_manager.admin.exception.UserWithIdNotExistsException;
import com.example.task_manager.auth.repo.AuthRepo;
import com.example.task_manager.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.example.task_manager.user.model.UserRoles.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock private AuthRepo authRepo;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private AdminService adminService;

    private static  final Long TEST_ID = 0L;
    private static final String TEST_USERNAME = "tom";
    private static final String TEST_PASSWORD = "pass1234";
    private static final String TEST_EMAIL = "tom@gmail.com";

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(TEST_ID);
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setEmail(TEST_EMAIL);
        testUser.setRole(USER);
    }

    @Test
    void testGetAllUsers() {
        adminService.getAllUsers();
        verify(authRepo).findAll();
    }

    @Test
    void testUpdateUserInformation() {
        when(authRepo.findById(anyLong())).thenReturn(Optional.of(testUser));

        adminService.updateUserInformation(testUser.getId(), testUser);

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(authRepo).save(captor.capture());

        assertThat(captor.getValue().getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(captor.getValue().getEmail()).isEqualTo(TEST_EMAIL);

        verify(passwordEncoder).encode(TEST_PASSWORD);
    }

    @Test
    void testUpdateInformationThrowUserByIdNotFound() {
        when(authRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.updateUserInformation(testUser.getId(), testUser))
                .isInstanceOf(UserWithIdNotExistsException.class)
                .hasMessageContaining(String.valueOf(TEST_ID));

        verify(authRepo, never()).save(any());
    }

    @Test
    void deleteUser() {
        when(authRepo.findById(anyLong())).thenReturn(Optional.of(testUser));

        adminService.deleteUser(TEST_ID, "admin name");

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(authRepo).delete(captor.capture());

        assertThat(captor.getValue()).isEqualTo(testUser);
    }

    @Test
    void testDeleteUserThrowUserByIdNotFound() {
        when(authRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.deleteUser(testUser.getId(), "admin name"))
                .isInstanceOf(UserWithIdNotExistsException.class)
                .hasMessageContaining(String.valueOf(TEST_ID));

        verify(authRepo, never()).delete(any());
    }

    @Test
    void testAdminDeleteHimself() {
        when(authRepo.findById(anyLong())).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> adminService.deleteUser(TEST_ID, TEST_USERNAME))
                .isInstanceOf(SelfDeleteException.class);

        verify(authRepo, never()).delete(any());
    }
}
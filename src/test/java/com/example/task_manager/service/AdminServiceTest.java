package com.example.task_manager.service;

import com.example.task_manager.dto.user.UserDto;
import com.example.task_manager.exception.SelfDeleteException;
import com.example.task_manager.exception.UserWithIdNotExistsException;
import com.example.task_manager.mapper.UserMapper;
import com.example.task_manager.repo.AuthRepo;
import com.example.task_manager.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.example.task_manager.model.user.UserRoles.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock private AuthRepo authRepo;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserMapper userMapper;

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
    void getAllUsers_shouldReturnAllUsers() {
        List<User> users = List.of(new User(), new User());
        List<UserDto> userDtos = List.of(new UserDto(), new UserDto());

        when(authRepo.findAll()).thenReturn(users);
        when(userMapper.userDtos(users)).thenReturn(userDtos);

        assertThat(adminService.getAllUsers()).isEqualTo(userDtos);
        verify(authRepo).findAll();
        verify(userMapper).userDtos(users);
    }

    @Test
    void updateUserInformation_shouldUpdateUserById() {
        when(authRepo.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn("encodedPassword");

        adminService.updateUserInformation(testUser.getId(), testUser);

        assertThat(testUser.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(testUser.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(testUser.getPassword()).isEqualTo("encodedPassword");

        verify(authRepo).findById(testUser.getId());
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(authRepo, never()).save(any());
    }

    @Test
    void updateUserInformation_shouldThrowUserWithIdNotExistsException_whenUserIdNotExists() {
        when(authRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.updateUserInformation(testUser.getId(), testUser))
                .isInstanceOf(UserWithIdNotExistsException.class)
                .hasMessageContaining(String.valueOf(TEST_ID));

        verify(authRepo, never()).save(any());
    }

    @Test
    void deleteUser_shouldDeleteUser_byId() {
        when(authRepo.findById(anyLong())).thenReturn(Optional.of(testUser));

        adminService.deleteUser(TEST_ID, "admin name");

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(authRepo).delete(captor.capture());

        assertThat(captor.getValue()).isEqualTo(testUser);
    }

    @Test
    void deleteUser_shouldThrowUserWithIdNotExistsException_whenUserIdNotExists() {
        when(authRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.deleteUser(testUser.getId(), "admin name"))
                .isInstanceOf(UserWithIdNotExistsException.class)
                .hasMessageContaining(String.valueOf(TEST_ID));

        verify(authRepo, never()).delete(any());
    }

    @Test
    void deleteUser_shouldThrowSelfDeleteException_whenUsernameEqualsAdminsUsername() {
        when(authRepo.findById(anyLong())).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> adminService.deleteUser(TEST_ID, TEST_USERNAME))
                .isInstanceOf(SelfDeleteException.class);

        verify(authRepo, never()).delete(any());
    }
}
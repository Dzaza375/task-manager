package com.example.task_manager.jwt.service;

import com.example.task_manager.auth.repo.AuthRepo;
import com.example.task_manager.auth.security.CustomUserDetails;
import com.example.task_manager.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.example.task_manager.user.model.UserRoles.USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {
    @Mock private AuthRepo authRepo;

    @InjectMocks MyUserDetailsService userDetailsService;

    private static final String TEST_USERNAME = "tom";
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(0L);
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword("password");
        testUser.setEmail("tom@gmail.com");
        testUser.setRole(USER);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        when(authRepo.findByUsername(anyString())).thenReturn(Optional.of(testUser));

         UserDetails userDetailsFromServiceMethod = userDetailsService.loadUserByUsername(TEST_USERNAME);

         UserDetails testUserDetails = new CustomUserDetails(testUser);

         assertThat(testUserDetails.getUsername())
                 .isEqualTo(userDetailsFromServiceMethod.getUsername());
         assertThat(testUserDetails.getPassword())
                 .isEqualTo(userDetailsFromServiceMethod.getPassword());
         assertThat(testUserDetails.getAuthorities())
                 .isEqualTo(userDetailsFromServiceMethod.getAuthorities());
    }

    @Test
    void loadByUsername_shouldThrowUsernameNotFoundException() {
        when(authRepo.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(TEST_USERNAME))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}
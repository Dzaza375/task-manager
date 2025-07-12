package com.example.task_manager.auth.repo;

import com.example.task_manager.user.model.User;
import com.example.task_manager.user.model.UserRoles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AuthRepoTest {
    @Autowired
    private AuthRepo testAuthRepo;

    private static final String TEST_USERNAME = "tom";

    private User createTestUser() {
        User user = new User();

        user.setUsername(TEST_USERNAME);
        user.setPassword("password");
        user.setEmail("tom@gmail.com");
        user.setRole(UserRoles.USER);

        return user;
    }

    @AfterEach
    void tearDown() {
        testAuthRepo.deleteAll();
    }

    @Test
    void existsByUsername_shouldReturnTrue() {
        User user = createTestUser();
        testAuthRepo.save(user);

        boolean exists = testAuthRepo.existsByUsername(TEST_USERNAME);
        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_shouldReturnFalse() {
        boolean exists = testAuthRepo.existsByUsername("nonexistent");
        assertThat(exists).isFalse();
    }

    @Test
    void findByUsername_shouldReturnTestUsername() {
        User user = createTestUser();
        testAuthRepo.save(user);

        User foundedUser = testAuthRepo.findByUsername(TEST_USERNAME)
                .orElseThrow();

        assertThat(foundedUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void findByUsername_shouldReturnEmptyOptional_whenUserDoesNotExist() {
        var result = testAuthRepo.findByUsername("nonexistent");
        assertThat(result).isEmpty();
    }
}
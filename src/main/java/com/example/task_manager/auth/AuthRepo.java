package com.example.task_manager.auth;

import com.example.task_manager.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepo extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}

package com.example.task_manager.service;

import com.example.task_manager.repo.AuthRepo;
import com.example.task_manager.exception.IncorrectPasswordException;
import com.example.task_manager.exception.UsernameAlreadyExistsException;
import com.example.task_manager.config.ApplicationConfig;
import com.example.task_manager.model.user.UserRoles;
import com.example.task_manager.dto.user.JwtRequest;
import com.example.task_manager.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.task_manager.model.user.UserRoles.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepo authRepo;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationConfig config;

    private void saveUser(JwtRequest jwtRequest, UserRoles role) {
        if (authRepo.existsByUsername(jwtRequest.getUsername())) {
            throw new UsernameAlreadyExistsException(jwtRequest.getUsername());
        }

        User userToSave = new User();
        userToSave.setUsername(jwtRequest.getUsername());
        userToSave.setPassword(passwordEncoder.encode(jwtRequest.getPassword()));
        userToSave.setEmail(jwtRequest.getEmail());
        userToSave.setRole(role);

        authRepo.save(userToSave);
    }

    public void register(JwtRequest jwtRequest) {
        saveUser(jwtRequest, USER);
    }

    public void adminRegister(JwtRequest jwtRequest, String adminCode) {
        if (!config.getAdminCode().equals(adminCode)) {
            throw new IncorrectPasswordException();
        }

        saveUser(jwtRequest, ADMIN);
    }
}

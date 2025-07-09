package com.example.task_manager.auth;

import com.example.task_manager.exceptions.IncorrectPasswordException;
import com.example.task_manager.exceptions.UsernameAlreadyExistsException;
import com.example.task_manager.security.ApplicationConfig;
import com.example.task_manager.security.UserRoles;
import com.example.task_manager.user.JwtRequest;
import com.example.task_manager.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.task_manager.security.UserRoles.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepo authRepo;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationConfig config;

    private void saveUser(JwtRequest jwtRequest, UserRoles role) {
        if (authRepo.existsByUsername(jwtRequest.getUsername())) {
            throw new UsernameAlreadyExistsException(String.format("Username %s is already used", jwtRequest.getUsername()));
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
            throw new IncorrectPasswordException("Incorrect admin password");
        }

        saveUser(jwtRequest, ADMIN);
    }
}

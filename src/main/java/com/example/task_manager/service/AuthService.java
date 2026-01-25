package com.example.task_manager.service;

import com.example.task_manager.repo.AuthRepo;
import com.example.task_manager.exception.IncorrectPasswordException;
import com.example.task_manager.exception.UsernameAlreadyExistsException;
import com.example.task_manager.config.ApplicationConfig;
import com.example.task_manager.model.user.UserRoles;
import com.example.task_manager.dto.user.UserDto;
import com.example.task_manager.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.task_manager.model.user.UserRoles.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepo authRepo;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationConfig config;

    private void saveUser(UserDto userDTO, UserRoles role) {
        if (authRepo.existsByUsername(userDTO.getUsername())) {
            throw new UsernameAlreadyExistsException(userDTO.getUsername());
        }

        User userToSave = new User();
        userToSave.setUsername(userDTO.getUsername());
        userToSave.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userToSave.setEmail(userDTO.getEmail());
        userToSave.setRole(role);

        authRepo.save(userToSave);
    }

    public void register(UserDto userDTO) {
        saveUser(userDTO, USER);
    }

    public void adminRegister(UserDto userDTO, String adminCode) {
        if (!config.getAdminCode().equals(adminCode)) {
            throw new IncorrectPasswordException();
        }

        saveUser(userDTO, ADMIN);
    }
}

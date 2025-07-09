package com.example.task_manager.auth;

import com.example.task_manager.exceptions.IncorrectPassword;
import com.example.task_manager.exceptions.UsernameAlreadyExists;
import com.example.task_manager.jwt.JwtResponses;
import com.example.task_manager.jwt.JwtService;
import com.example.task_manager.security.ApplicationConfig;
import com.example.task_manager.security.UserRoles;
import com.example.task_manager.user.User;
import com.example.task_manager.user.UserDto;
import com.example.task_manager.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.task_manager.security.UserRoles.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepo authRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ApplicationConfig config;
    private final JwtService jwtService;

    private void saveUser(UserDto userDto, UserRoles role) {
        if (authRepo.existsByUsername(userDto.getUsername())) {
            throw new UsernameAlreadyExists(String.format("Username %s is already used", userDto.getUsername()));
        }

        User userToSave = new User();
        userToSave.setUsername(userDto.getUsername());
        userToSave.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userToSave.setEmail(userDto.getEmail());
        userToSave.setRole(role);

        userMapper.userToUserDto(authRepo.save(userToSave));
    }

    public void register(UserDto userDto) {
        saveUser(userDto, USER);
    }

    public void adminRegister(UserDto userDto, String adminCode) {
        if (!config.getAdminCode().equals(adminCode)) {
            throw new IncorrectPassword("Incorrect admin password");
        }

        saveUser(userDto, ADMIN);
    }

    public JwtResponses login(UserDto userDto) {
        return null;
    }
}

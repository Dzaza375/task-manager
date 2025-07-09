package com.example.task_manager.auth;

import com.example.task_manager.exceptions.IncorrectPassword;
import com.example.task_manager.exceptions.UsernameAlreadyExists;
import com.example.task_manager.jwt.JwtResponses;
import com.example.task_manager.jwt.JwtService;
import com.example.task_manager.security.ApplicationConfig;
import com.example.task_manager.security.UserRoles;
import com.example.task_manager.user.JwtRequest;
import com.example.task_manager.user.User;
import com.example.task_manager.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.task_manager.security.UserRoles.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepo authRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ApplicationConfig config;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private void saveUser(JwtRequest jwtRequest, UserRoles role) {
        if (authRepo.existsByUsername(jwtRequest.getUsername())) {
            throw new UsernameAlreadyExists(String.format("Username %s is already used", jwtRequest.getUsername()));
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
            throw new IncorrectPassword("Incorrect admin password");
        }

        saveUser(jwtRequest, ADMIN);
    }

    public JwtResponses login(JwtRequest jwtRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.generateToken((UserDetails) authentication.getPrincipal());

        return new JwtResponses(jwt, LocalDateTime.now());
    }
}

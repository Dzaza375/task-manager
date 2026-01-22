package com.example.task_manager.service;

import com.example.task_manager.exception.SelfDeleteException;
import com.example.task_manager.exception.UserWithIdNotExistsException;
import com.example.task_manager.repo.AuthRepo;
import com.example.task_manager.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final AuthRepo authRepo;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return authRepo.findAll();
    }

    public void updateUserInformation(Long userId, User user) {
        User userToUpdate = authRepo.findById(userId)
                .orElseThrow(() -> new UserWithIdNotExistsException(userId));

        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        userToUpdate.setEmail(user.getEmail());
    }

    public void deleteUser(Long userId, String adminName) {
        User userToDelete = authRepo.findById(userId)
                .orElseThrow(() -> new UserWithIdNotExistsException(userId));

        if (adminName.equals(userToDelete.getUsername())) {
            throw new SelfDeleteException();
        }

        authRepo.delete(userToDelete);
    }
}

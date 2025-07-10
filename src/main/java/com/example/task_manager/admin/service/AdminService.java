package com.example.task_manager.admin.service;

import com.example.task_manager.admin.exception.SelfDeleteException;
import com.example.task_manager.admin.exception.UserWithIdNotExistsException;
import com.example.task_manager.auth.repo.AuthRepo;
import com.example.task_manager.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AuthRepo authRepo;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return authRepo.findAll();
    }

    public void updateUserInformation(Long userId, User user) {
        User userToUpdate = authRepo.findById(userId)
                .orElseThrow(() -> new UserWithIdNotExistsException(String.format("User with id %s doesn't exist", userId)));

        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        userToUpdate.setEmail(user.getEmail());

        authRepo.save(userToUpdate);
    }

    public void deleteUser(Long userId, String adminName) {
        User userToDelete = authRepo.findById(userId)
                .orElseThrow(() -> new UserWithIdNotExistsException(String.format("User with id %s doesn't exist", userId)));

        if (adminName.equals(userToDelete.getUsername())) {
            throw new SelfDeleteException("You can't delete yourself");
        }

        authRepo.delete(userToDelete);
    }
}

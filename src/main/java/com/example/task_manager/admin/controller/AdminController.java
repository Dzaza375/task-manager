package com.example.task_manager.admin.controller;

import com.example.task_manager.admin.service.AdminService;
import com.example.task_manager.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('user:read')")
    public List<User> getUsers() {
        return adminService.getAllUsers();
    }

    @PutMapping("/{userId}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('user:write')")
    public void updateUser(@PathVariable("userId") Long userId,
                           @RequestBody @Valid User user) {
        adminService.updateUserInformation(userId, user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('user:write')")
    public void deleteUser(@PathVariable("userId") Long userid) {
        adminService.deleteUser(userid, getCurrentUsername());
    }
}

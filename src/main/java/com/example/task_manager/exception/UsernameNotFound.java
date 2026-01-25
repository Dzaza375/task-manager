package com.example.task_manager.exception;

import org.springframework.http.HttpStatus;

public class UsernameNotFound extends BusinessException {
    public UsernameNotFound(String username) {
        super(
                ErrorCode.USER_NOT_FOUND,
                HttpStatus.CONFLICT,
                "User with username " + username + " not found"
        );
    }
}

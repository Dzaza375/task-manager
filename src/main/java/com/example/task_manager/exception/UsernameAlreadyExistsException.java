package com.example.task_manager.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends BusinessException {
    public UsernameAlreadyExistsException(String username) {
        super(
                ErrorCode.ALREADY_EXISTS,
                HttpStatus.CONFLICT,
                "User with username " + username + " already exists"
        );
    }
}

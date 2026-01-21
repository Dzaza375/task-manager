package com.example.task_manager.exception;

import org.springframework.http.HttpStatus;

public class UserWithUsernameNotExistsException extends BusinessException {
    public UserWithUsernameNotExistsException(String username) {
        super(
                ErrorCode.USER_NOT_EXISTS,
                HttpStatus.NOT_FOUND,
                "User with username " + username + " not exists"
        );
    }
}

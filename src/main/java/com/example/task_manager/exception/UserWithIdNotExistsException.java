package com.example.task_manager.exception;

import org.springframework.http.HttpStatus;

public class UserWithIdNotExistsException extends BusinessException {
    public UserWithIdNotExistsException(Long userId) {
        super(
                ErrorCode.USER_NOT_EXISTS,
                HttpStatus.NOT_FOUND,
                "User with id " + userId + " not exists"
        );
    }
}

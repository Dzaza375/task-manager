package com.example.task_manager.exception.advice;

import com.example.task_manager.exception.BusinessException;
import com.example.task_manager.exception.ErrorCode;
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

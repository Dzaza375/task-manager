package com.example.task_manager.exception;

import org.springframework.http.HttpStatus;

public class IncorrectPasswordException extends BusinessException {
    public IncorrectPasswordException() {
        super(
                ErrorCode.INCORRECT_PASSWORD,
                HttpStatus.UNAUTHORIZED,
                "Incorrect password for current user"
        );
    }
}

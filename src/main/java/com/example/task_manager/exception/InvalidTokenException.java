package com.example.task_manager.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException(String message) {
        super(
                ErrorCode.INVALID_TOKEN,
                HttpStatus.UNAUTHORIZED,
                message
        );
    }
}

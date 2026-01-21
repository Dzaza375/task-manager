package com.example.task_manager.exception;

import org.springframework.http.HttpStatus;

public class NotEnoughRightException extends BusinessException {
    public NotEnoughRightException() {
        super(
                ErrorCode.ACCESS_DENIED,
                HttpStatus.FORBIDDEN,
                "You don't have enough rights to do this"
        );
    }
}

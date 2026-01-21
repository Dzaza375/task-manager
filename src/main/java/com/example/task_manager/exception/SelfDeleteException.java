package com.example.task_manager.exception;

import org.springframework.http.HttpStatus;

public class SelfDeleteException extends BusinessException {
    public SelfDeleteException() {
        super(
                ErrorCode.SELF_DELETE,
                HttpStatus.FORBIDDEN,
                "You can't delete yourself"
        );
    }
}

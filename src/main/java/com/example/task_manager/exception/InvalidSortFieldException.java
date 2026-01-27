package com.example.task_manager.exception;

import org.springframework.http.HttpStatus;

public class InvalidSortFieldException extends BusinessException {
    public InvalidSortFieldException(String field) {
        super(
                ErrorCode.INCORRECT_SORTING,
                HttpStatus.FORBIDDEN,
                "Sorting by field " + field + " is not allowed"
        );
    }
}

package com.example.task_manager.exception;

import org.springframework.http.HttpStatus;

public class TaskNotFoundException extends BusinessException {
    public TaskNotFoundException(Long taskId) {
        super(
                ErrorCode.TASK_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                "Task with id " + taskId + " not found"
        );
    }
}

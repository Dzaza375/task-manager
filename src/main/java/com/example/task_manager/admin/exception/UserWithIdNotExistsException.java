package com.example.task_manager.admin.exception;

public class UserWithIdNotExistsException extends RuntimeException {
    public UserWithIdNotExistsException(String message) {
        super(message);
    }
}

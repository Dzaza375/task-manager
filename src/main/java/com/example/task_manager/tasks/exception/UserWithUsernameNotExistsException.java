package com.example.task_manager.tasks.exception;

public class UserWithUsernameNotExistsException extends RuntimeException {
    public UserWithUsernameNotExistsException(String message) {
        super(message);
    }
}

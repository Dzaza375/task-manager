package com.example.task_manager.admin.exception;

public class SelfDeleteException extends RuntimeException {
    public SelfDeleteException(String message) {
        super(message);
    }
}

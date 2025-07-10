package com.example.task_manager.tasks.exception;

public class NotEnoughRightException extends RuntimeException {
    public NotEnoughRightException(String message) {
        super(message);
    }
}

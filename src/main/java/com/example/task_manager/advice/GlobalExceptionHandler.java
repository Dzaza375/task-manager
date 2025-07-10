package com.example.task_manager.advice;

import com.example.task_manager.admin.exception.SelfDeleteException;
import com.example.task_manager.admin.exception.UserWithIdNotExistsException;
import com.example.task_manager.auth.exception.IncorrectPasswordException;
import com.example.task_manager.auth.exception.UsernameAlreadyExistsException;
import com.example.task_manager.jwt.exception.InvalidTokenException;
import com.example.task_manager.tasks.exception.NotEnoughRightException;
import com.example.task_manager.tasks.exception.TaskNotFoundException;
import com.example.task_manager.tasks.exception.UserWithUsernameNotExistsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserWithIdNotExistsException.class)
    @ResponseStatus(NOT_FOUND)
    public Map<String, String> handleUserWithIdNotExistsException(UserWithIdNotExistsException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(NotEnoughRightException.class)
    @ResponseStatus(FORBIDDEN)
    public Map<String, String> handleNotEnoughRightException(NotEnoughRightException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Map<String, String> handleTaskNotFoundException(TaskNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(UserWithUsernameNotExistsException.class)
    @ResponseStatus(NOT_FOUND)
    public Map<String, String> handleUserWithUsernameNotExistsException(UserWithUsernameNotExistsException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(UNAUTHORIZED)
    public Map<String, String> handleInvalidTokenException(InvalidTokenException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(UNAUTHORIZED)
    public Map<String, String> handleIncorrectPasswordException(IncorrectPasswordException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(CONFLICT)
    public Map<String, String> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(SelfDeleteException.class)
    @ResponseStatus(FORBIDDEN)
    public Map<String, String> handleSelfDeleteException(SelfDeleteException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnexpectedErrors(Exception ex) {
        return Map.of("error", "Unexpected error occurred: " + ex.getMessage());
    }
}

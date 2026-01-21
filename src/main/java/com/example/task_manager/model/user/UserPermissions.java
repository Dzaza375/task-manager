package com.example.task_manager.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserPermissions {
    TASK_READ("task:read"),
    TASK_WRITE("task:write"),
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String permission;
}

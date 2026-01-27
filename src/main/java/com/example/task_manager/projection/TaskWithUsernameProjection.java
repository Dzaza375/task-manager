package com.example.task_manager.projection;

import com.example.task_manager.model.task.TaskStatus;

import java.time.LocalDate;

public interface TaskWithUsernameProjection {
    String getTitle();
    String getDescription();
    LocalDate getDueDate();
    TaskStatus getStatus();
    UserInfo getAssignedTo();

    interface UserInfo {
        String getUsername();
    }
}
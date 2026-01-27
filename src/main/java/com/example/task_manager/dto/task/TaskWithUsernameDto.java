package com.example.task_manager.dto.task;

import com.example.task_manager.model.task.TaskStatus;

import java.time.LocalDate;

public record TaskWithUsernameDto (String title,
                                   String description,
                                   LocalDate dueDate,
                                   TaskStatus status,
                                   String username) {}

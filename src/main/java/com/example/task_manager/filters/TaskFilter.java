package com.example.task_manager.filters;

import com.example.task_manager.model.task.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskFilter {
    private String title;
    private String description;
    private LocalDate dueDateFrom;
    private LocalDate dueDateTo;
    private TaskStatus status;
}

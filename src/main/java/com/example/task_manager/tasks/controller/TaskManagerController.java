package com.example.task_manager.tasks.controller;

import com.example.task_manager.tasks.model.Task;
import com.example.task_manager.tasks.service.TaskManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskManagerController {
    private final TaskManagerService taskManagerService;

    @GetMapping
    @PreAuthorize("hasAuthority('task:read')")
    public List<Task> getAllTasks() {
        return taskManagerService.getAllTasks();
    }

    //POST	/api/tasks	Создать задачу	✅ USER/ADMIN
    //PUT	/api/tasks/{id}	Обновить задачу	✅ USER
    //DELETE	/api/tasks/{id}	Удалить задачу	✅ ADMIN
}

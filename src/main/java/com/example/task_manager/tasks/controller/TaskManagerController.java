package com.example.task_manager.tasks.controller;

import com.example.task_manager.tasks.dto.TaskDto;
import com.example.task_manager.tasks.mapper.TaskMapper;
import com.example.task_manager.tasks.model.Task;
import com.example.task_manager.tasks.service.TaskManagerService;
import com.example.task_manager.user.model.UserRoles;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskManagerController {
    private final TaskManagerService taskManagerService;
    private final TaskMapper taskMapper;

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('task:read')")
    public List<?> getAllTasks() {
        boolean isAdmin = getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        List<Task> allTasks = taskManagerService.getAllTasks();
        if (isAdmin) return allTasks;
        return taskMapper.taskDtos(allTasks);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('task:write')")
    public void createNewTask(@RequestBody @Valid TaskDto taskDto) {
        String username = getAuthentication().getName();
        taskManagerService.createTask(taskDto, username);
    }

    //PUT	/api/tasks/{id}	Обновить задачу	✅ USER
    //DELETE	/api/tasks/{id}	Удалить задачу	✅ ADMIN
}

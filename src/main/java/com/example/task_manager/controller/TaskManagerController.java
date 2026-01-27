package com.example.task_manager.controller;

import com.example.task_manager.dto.task.TaskDto;
import com.example.task_manager.dto.task.TaskWithUsernameDto;
import com.example.task_manager.pagination.PageResponse;
import com.example.task_manager.service.TaskManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskManagerController {
    private final TaskManagerService taskManagerService;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private boolean checkAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('task:read')")
    public PageResponse<TaskWithUsernameDto> getAllTasks(
            @PageableDefault(sort = "dueDate", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return taskManagerService.getAllTasks(pageable);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('task:write')")
    public void createNewTask(@RequestBody @Valid TaskDto taskDto) {
        String username = getCurrentUsername();
        taskManagerService.createTask(taskDto, username);
    }

    @PutMapping("/{taskId}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('task:write')")
    public void updateTask(@PathVariable("taskId") Long taskId,
                           @RequestBody @Valid TaskDto taskDto) {
        taskManagerService.updateTask(
                taskId,
                taskDto,
                getCurrentUsername(),
                checkAdminRole()
        );
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('task:write')")
    public void deleteTask(@PathVariable("taskId") Long taskId) {
        taskManagerService.deleteTask(taskId, getCurrentUsername(), checkAdminRole());
    }
}

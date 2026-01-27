package com.example.task_manager.service;

import com.example.task_manager.dto.task.TaskWithUsernameDto;
import com.example.task_manager.mapper.TaskProjectionMapper;
import com.example.task_manager.pagination.PageResponse;
import com.example.task_manager.pagination.PageableValidator;
import com.example.task_manager.projection.TaskWithUsernameProjection;
import com.example.task_manager.repo.AuthRepo;
import com.example.task_manager.dto.task.TaskDto;
import com.example.task_manager.exception.NotEnoughRightException;
import com.example.task_manager.exception.TaskNotFoundException;
import com.example.task_manager.exception.UserWithUsernameNotExistsException;
import com.example.task_manager.model.task.Task;
import com.example.task_manager.repo.TaskManagerRepo;
import com.example.task_manager.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskManagerService {
    private final TaskManagerRepo taskManagerRepo;
    private final AuthRepo authRepo;
    private final PageableValidator pageableValidator;
    private final TaskProjectionMapper taskProjectionMapper;

    @Transactional(readOnly = true)
    public PageResponse<TaskWithUsernameDto> getAllTasks(Pageable pageable) {
        Pageable validated = pageableValidator.validate(pageable);

        Page<TaskWithUsernameProjection> page = taskManagerRepo.findAllBy(validated);

        Page<TaskWithUsernameDto> dtoPage = page.map(taskProjectionMapper::toDto);

        return PageResponse.from(dtoPage);
    }

    public void createTask(TaskDto taskDto, String username) {
        User user = authRepo.findByUsername(username)
                .orElseThrow(() -> new UserWithUsernameNotExistsException(username));

        Task taskToSave = new Task();
        taskToSave.setTitle(taskDto.getTitle());
        taskToSave.setDescription(taskDto.getDescription());
        taskToSave.setDueDate(taskDto.getDueDate());
        taskToSave.setStatus(taskDto.getStatus());
        taskToSave.setAssignedTo(user);

        taskManagerRepo.save(taskToSave);
    }

    public void updateTask(Long taskId,
                           TaskDto taskDto,
                           String username,
                           boolean isAdmin) {
        Task taskToUpdate = taskManagerRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (!isAdmin && !username.equals(taskToUpdate.getAssignedTo().getUsername())) {
            throw new NotEnoughRightException();
        }

        taskToUpdate.setTitle(taskDto.getTitle());
        taskToUpdate.setDescription(taskDto.getDescription());
        taskToUpdate.setDueDate(taskDto.getDueDate());
        taskToUpdate.setStatus(taskDto.getStatus());
    }

    public void deleteTask(Long taskId, String username, boolean isAdmin) {
        Task taskToDelete = taskManagerRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (!isAdmin && !username.equals(taskToDelete.getAssignedTo().getUsername())) {
            throw new NotEnoughRightException();
        }

        taskManagerRepo.delete(taskToDelete);
    }
}

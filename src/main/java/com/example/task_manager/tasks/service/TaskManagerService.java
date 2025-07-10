package com.example.task_manager.tasks.service;

import com.example.task_manager.auth.repo.AuthRepo;
import com.example.task_manager.tasks.dto.TaskDto;
import com.example.task_manager.tasks.exception.NotEnoughRightException;
import com.example.task_manager.tasks.exception.TaskNotFoundException;
import com.example.task_manager.tasks.exception.UserNotExistsException;
import com.example.task_manager.tasks.model.Task;
import com.example.task_manager.tasks.repo.TaskManagerRepo;
import com.example.task_manager.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskManagerService {
    private final TaskManagerRepo taskManagerRepo;
    private final AuthRepo authRepo;

    public List<Task> getAllTasks() {
        return taskManagerRepo.findAll();
    }

    public void createTask(TaskDto taskDto, String username) {
        User user = authRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotExistsException("User with this Id is not exists"));

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
                .orElseThrow(() -> new TaskNotFoundException("There is not task with id " + taskId));

        if (!isAdmin && !username.equals(taskToUpdate.getAssignedTo().getUsername())) {
            throw new NotEnoughRightException("You can't update this task!");
        }

        taskToUpdate.setTitle(taskDto.getTitle());
        taskToUpdate.setDescription(taskDto.getDescription());
        taskToUpdate.setDueDate(taskDto.getDueDate());
        taskToUpdate.setStatus(taskDto.getStatus());

        taskManagerRepo.save(taskToUpdate);
    }

    public void deleteTask(Long taskId, String username, boolean isAdmin) {
        Task taskToDelete = taskManagerRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("There is not task with id " + taskId));

        if (!isAdmin && !username.equals(taskToDelete.getAssignedTo().getUsername())) {
            throw new NotEnoughRightException("You can't delete this task!");
        }

        taskManagerRepo.delete(taskToDelete);
    }
}

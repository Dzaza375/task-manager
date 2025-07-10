package com.example.task_manager.tasks.service;

import com.example.task_manager.tasks.model.Task;
import com.example.task_manager.tasks.repo.TaskManagerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskManagerService {
    private final TaskManagerRepo taskManagerRepo;

    public List<Task> getAllTasks() {
        return taskManagerRepo.findAll();
    }
}

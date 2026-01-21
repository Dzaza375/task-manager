package com.example.task_manager.repo;

import com.example.task_manager.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskManagerRepo extends JpaRepository<Task, Long> {
}

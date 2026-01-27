package com.example.task_manager.repo;

import com.example.task_manager.model.task.Task;
import com.example.task_manager.projection.TaskWithUsernameProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskManagerRepo extends JpaRepository<Task, Long> {
    Page<TaskWithUsernameProjection> findAllBy(Pageable pageable);
}

package com.example.task_manager.tasks.mapper;

import com.example.task_manager.tasks.dto.TaskDto;
import com.example.task_manager.tasks.model.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto taskDto(Task task);
    List<TaskDto> taskDtos(List<Task> tasks);
}

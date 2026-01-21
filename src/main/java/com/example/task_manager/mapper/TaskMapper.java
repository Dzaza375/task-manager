package com.example.task_manager.mapper;

import com.example.task_manager.dto.task.TaskDto;
import com.example.task_manager.model.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "id", target = "taskId")
    TaskDto taskDto(Task task);

    List<TaskDto> taskDtos(List<Task> tasks);
}

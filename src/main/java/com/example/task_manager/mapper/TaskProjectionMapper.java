package com.example.task_manager.mapper;

import com.example.task_manager.dto.task.TaskWithUsernameDto;
import com.example.task_manager.projection.TaskWithUsernameProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskProjectionMapper {
    @Mapping(source = "assignedTo.username", target = "username")
    TaskWithUsernameDto toDto(TaskWithUsernameProjection projection);
}

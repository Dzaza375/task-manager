package com.example.task_manager.mapper;

import com.example.task_manager.dto.user.UserDto;
import com.example.task_manager.model.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userDto(User user);
    List<UserDto> userDtos(List<User> users);
}

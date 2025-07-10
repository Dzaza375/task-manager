package com.example.task_manager.user.mapper;

import com.example.task_manager.user.dto.JwtRequest;
import com.example.task_manager.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    JwtRequest userToJwtRequest(User user);
    User userDtoToUser(JwtRequest jwtRequest);
}

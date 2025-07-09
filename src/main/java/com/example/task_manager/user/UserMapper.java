package com.example.task_manager.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    JwtRequest userToJwtRequest(User user);
    User userDtoToUser(JwtRequest jwtRequest);
}

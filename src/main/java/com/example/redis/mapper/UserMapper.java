package com.example.redis.mapper;

import com.example.redis.dto.UserCreateDTO;
import com.example.redis.dto.UserDTO;
import com.example.redis.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toUser(UserCreateDTO userCreateDTO);

  UserDTO toUserDTO(User user);

}

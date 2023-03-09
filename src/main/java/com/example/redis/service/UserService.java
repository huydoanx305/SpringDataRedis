package com.example.redis.service;

import com.example.redis.dto.UserCreateDTO;
import com.example.redis.dto.UserDTO;

public interface UserService {

  UserDTO getUserById(String id);

  UserDTO createUser(UserCreateDTO userCreateDTO);

}

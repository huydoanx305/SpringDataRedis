package com.example.redis.service.impl;

import com.example.redis.dto.UserCreateDTO;
import com.example.redis.dto.UserDTO;
import com.example.redis.entity.User;
import com.example.redis.exception.NotFoundException;
import com.example.redis.mapper.UserMapper;
import com.example.redis.repository.UserRepository;
import com.example.redis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final String USER_REDIS_KEY = "user:%s";

  private final UserRepository userRepository;

  private final RedisTemplate<String, Object> redisTemplate;

  private final UserMapper userMapper;

  @Override
  public UserDTO getUserById(String id) {
    UserDTO userDtoRedis = (UserDTO) redisTemplate.opsForValue().get(String.format(USER_REDIS_KEY, id));
    if(ObjectUtils.isEmpty(userDtoRedis)) {
      System.out.println("Không có user trên redis");
      User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không có người dùng này"));
      UserDTO userDTO = userMapper.toUserDTO(user);
      redisTemplate.opsForValue().set(String.format(USER_REDIS_KEY, user.getId()), userDTO);
      return userDTO;
    } else {
      return userDtoRedis;
    }
  }

  @Override
  public UserDTO createUser(UserCreateDTO userCreateDTO) {
    User user = userMapper.toUser(userCreateDTO);
    userRepository.save(user);
    UserDTO userDTO = userMapper.toUserDTO(user);
    redisTemplate.opsForValue().set(String.format(USER_REDIS_KEY, user.getId()), userDTO);
    return userDTO;
  }

}

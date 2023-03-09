package com.example.redis.controller;

import com.example.redis.dto.UserCreateDTO;
import com.example.redis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;

  @GetMapping("/user/{id}")
  public ResponseEntity<?> createUser(@PathVariable String id) {
    return ResponseEntity.ok().body(userService.getUserById(id));
  }

  @PostMapping("/user")
  public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateDTO userCreateDTO) {
    return ResponseEntity.ok().body(userService.createUser(userCreateDTO));
  }

}

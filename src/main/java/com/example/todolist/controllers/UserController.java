package com.example.todolist.controllers;

import com.example.todolist.models.User;
import com.example.todolist.repositories.UserRepository;
import com.example.todolist.schemas.UserSchema;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @PostMapping("/signup")
  public ResponseEntity<Map<String, ?>> signup(@RequestBody User inputUser) {
    User checkUser = userRepository.findByUsername(inputUser.getUsername());
    if (checkUser != null) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "Username has been taken!");
      return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    User newUser = new User();
    newUser.setUsername(inputUser.getUsername());
    String hashedPassword = BCrypt.hashpw(inputUser.getPassword(), BCrypt.gensalt());
    newUser.setPassword(hashedPassword);
    newUser.setName(inputUser.getName());
    userRepository.save(newUser);
    Map<String, String> successResponse = new HashMap<>();
    successResponse.put("message", "Created successfully!");
    return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, ?>> login(@RequestBody User inputUser) {
    User checkUser = userRepository.findByUsername(inputUser.getUsername());
    if (checkUser == null) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "Username cannot be found!");
      return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    if (!BCrypt.checkpw(inputUser.getPassword(), checkUser.getPassword())) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "Wrong password!");
      return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    Map<String, String> successResponse = new HashMap<>();
    successResponse.put("userId", String.valueOf(checkUser.getId()));
    successResponse.put("fullName", checkUser.getName());
    return new ResponseEntity<>(successResponse, HttpStatus.OK);
  }

  @GetMapping("/user")
  public ResponseEntity<Map<String, ?>> getUser(@RequestHeader("userId") Long id) {
    User user = userRepository.findById(id).orElse(null);
    if(user == null) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "User cannot be found!");
      return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    Map<String, Object> successResponse = new HashMap<>();
    successResponse.put("user", user);
    return new ResponseEntity<>(successResponse, HttpStatus.OK);
  }

  @PutMapping("/user")
  public ResponseEntity<Map<String, ?>> editUser(@RequestHeader("userId") Long id, @RequestBody UserSchema inputUser) {
    User updateUser = userRepository.findById(id).orElse(null);

    if (updateUser == null) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "User cannot be found!");
      return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    if (inputUser.getName() != null) {
      updateUser.setName(inputUser.getName());
    }
    if (inputUser.getBirthday() != null) {
      updateUser.setBirthday(inputUser.getBirthday());
    }
    if (inputUser.getSex() != null) {
      updateUser.setSex(inputUser.getSex());
    }

    if (inputUser.getOldPassword() != null && inputUser.getNewPassword() != null) {
      boolean isEqual = BCrypt.checkpw(inputUser.getOldPassword(), updateUser.getPassword());
      if (!isEqual) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Wrong password!");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
      }
      if (inputUser.getOldPassword().equals(inputUser.getNewPassword())) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Try another new password!");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
      }
      String hashPw = BCrypt.hashpw(inputUser.getNewPassword(), BCrypt.gensalt());
      updateUser.setPassword(hashPw);
    }

    userRepository.save(updateUser);
    Map<String, String> successResponse = new HashMap<>();
    successResponse.put("message", "Updated successfully!");
    return new ResponseEntity<>(successResponse, HttpStatus.OK);
  }
}

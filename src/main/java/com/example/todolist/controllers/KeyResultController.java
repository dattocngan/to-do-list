package com.example.todolist.controllers;

import com.example.todolist.models.*;
import com.example.todolist.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/keyResults")
public class KeyResultController {
  @Autowired
  private ObjectiveRepository objectiveRepository;
  @Autowired
  private KeyResultRepository keyResultRepository;

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, ?>> deleteKeyResultById(@RequestHeader("userId") Long userId, @PathVariable long id) {
    KeyResult keyResult = keyResultRepository.findById(id).orElse(null);
    if (keyResult == null) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "Key result cannot be found!");
      return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    Objective objective = objectiveRepository.findById(keyResult.getObjectiveId()).orElse(null);
    if (objective != null && !Objects.equals(objective.getUserId(), userId)) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "Not allowed!");
      return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    keyResultRepository.delete(keyResult);

    Map<String, String> successResponse = new HashMap<>();
    successResponse.put("message", "Deleted successfully!");
    return ResponseEntity.status(200).body(successResponse);
  }
}

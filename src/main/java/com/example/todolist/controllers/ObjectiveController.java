package com.example.todolist.controllers;

import com.example.todolist.models.KeyResult;
import com.example.todolist.models.Objective;
import com.example.todolist.repositories.KeyResultRepository;
import com.example.todolist.repositories.ObjectiveRepository;
import com.example.todolist.schemas.ObjectiveSchema;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/objectives")
public class ObjectiveController {
  @Autowired
  private ObjectiveRepository objectiveRepository;
  @Autowired
  private KeyResultRepository keyResultRepository;

  @GetMapping
  public ResponseEntity<Map<String, ?>> getObjectives(@RequestHeader("userId") Long userId) {
      List<Objective> objectives = objectiveRepository.findByUserId(userId);
      ModelMapper modelMapper = new ModelMapper();
      List<ObjectiveSchema> convertedObjectives = objectives.stream()
              .map(objective -> modelMapper.map(objective, ObjectiveSchema.class))
              .toList();
      for(ObjectiveSchema objective : convertedObjectives) {
        List<KeyResult> keyResults = keyResultRepository.findByObjectiveId(objective.getId());
        objective.setKeyResults(keyResults);
      }
      Map<String, List<ObjectiveSchema>> successResponse = new HashMap<>();
      successResponse.put("objectives", convertedObjectives);
      return ResponseEntity.status(200).body(successResponse);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, ?>> getObjectiveById(@RequestHeader("userId") Long userId, @PathVariable long id) {
    Objective objective = objectiveRepository.findByIdAndUserId(id, userId);
    if (objective == null) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "Mục tiêu không thể tìm thấy!");
      return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    ModelMapper modelMapper = new ModelMapper();
    ObjectiveSchema convertedObjective = modelMapper.map(objective, ObjectiveSchema.class);
    List<KeyResult> keyResults = keyResultRepository.findByObjectiveId(convertedObjective.getId());
    convertedObjective.setKeyResults(keyResults);
    Map<String, ObjectiveSchema> successResponse = new HashMap<>();
    successResponse.put("objective", convertedObjective);
    return ResponseEntity.status(200).body(successResponse);
  }

  @PostMapping
  public ResponseEntity<Map<String, ?>> createObjective(@RequestHeader("userId") Long userId, @RequestBody ObjectiveSchema inputObjective) {
    Objective newObjective = new Objective();
    newObjective.setContent(inputObjective.getContent());
    newObjective.setReason(inputObjective.getReason());
    newObjective.setType(inputObjective.getType());
    newObjective.setStatus(inputObjective.getStatus());
    newObjective.setDeadlineAt(inputObjective.getDeadlineAt());
    newObjective.setUserId(userId);
    Objective objective = objectiveRepository.save(newObjective);

    List<KeyResult> keyResults = inputObjective.getKeyResults();
    for(KeyResult keyResult : keyResults) {
      keyResult.setObjectiveId(objective.getId());
      keyResultRepository.save(keyResult);
    }

    Map<String, String> successResponse = new HashMap<>();
    successResponse.put("message", "Tạo mục tiêu thành công!");
    return ResponseEntity.status(201).body(successResponse);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, ?>> editObjectiveById(@RequestHeader("userId") Long userId, @PathVariable long id, @RequestBody ObjectiveSchema inputObjective) {
    Objective objective = objectiveRepository.findByIdAndUserId(id, userId);
    if (objective == null) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "Mục tiêu không thể tìm thấy!");
      return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    if (inputObjective.getType() != null) {
      objective.setType(inputObjective.getType());
    }
    if (inputObjective.getContent() != null) {
      objective.setContent(inputObjective.getContent());
    }
    if (inputObjective.getReason() != null) {
      objective.setReason(inputObjective.getReason());
    }
    if (inputObjective.getStatus() != null) {
      objective.setStatus(inputObjective.getStatus());
    }
    if (inputObjective.getDeadlineAt() != null) {
      objective.setDeadlineAt(inputObjective.getDeadlineAt());
    }
    objectiveRepository.save(objective);

    for (KeyResult keyResult : inputObjective.getKeyResults()) {
      if (keyResult.getId() != null) {
        KeyResult editKeyResult = keyResultRepository.findById(keyResult.getId()).orElse(null);
        if (editKeyResult != null) {
          if (keyResult.getContent() != null) {
            editKeyResult.setContent(keyResult.getContent());
          }
          if (keyResult.getUnit() != null) {
            editKeyResult.setUnit(keyResult.getUnit());
          }
          if (keyResult.getTarget() != 0) {
            editKeyResult.setTarget(keyResult.getTarget());
          }
          if (keyResult.getCurrentAchievement() != 0) {
            editKeyResult.setCurrentAchievement(keyResult.getCurrentAchievement());
          }
          if (keyResult.getDeadlineAt() != null) {
            editKeyResult.setDeadlineAt(keyResult.getDeadlineAt());
          }
          keyResultRepository.save(editKeyResult);
        }
      } else {
        keyResult.setObjectiveId(objective.getId());
        keyResultRepository.save(keyResult);
      }
    }

    Map<String, String> successResponse = new HashMap<>();
    successResponse.put("message", "Cập nhật thành công!");
    return ResponseEntity.status(200).body(successResponse);
  }

  @Transactional
  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, ?>> deleteObjectiveById(@RequestHeader("userId") Long userId, @PathVariable long id) {
    Objective obj = objectiveRepository.findByIdAndUserId(id, userId);
    if (obj == null) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "Mục tiêu không thể tìm thấy!");
      return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    objectiveRepository.delete(obj);
    keyResultRepository.deleteByObjectiveId(id);

    Map<String, String> successResponse = new HashMap<>();
    successResponse.put("message", "Xoá thành công!");
    return ResponseEntity.status(200).body(successResponse);
  }
}

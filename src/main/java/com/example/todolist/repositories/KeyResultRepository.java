package com.example.todolist.repositories;

import com.example.todolist.models.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {
  List<KeyResult> findByObjectiveId(Long objectiveId);
  void deleteByObjectiveId(Long objectiveId);
}

package com.example.todolist.repositories;

import com.example.todolist.models.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
  List<Objective> findByUserId(Long userId);

  @Query("SELECT o FROM Objective o WHERE o.id = :objectiveId AND o.userId = :userId")
  Objective findByIdAndUserId(@Param("objectiveId") Long objectiveId, @Param("userId") Long userId);
}

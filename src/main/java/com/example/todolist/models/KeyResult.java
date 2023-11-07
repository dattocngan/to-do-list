package com.example.todolist.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "key_results")
public class KeyResult {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "content", nullable = false)
  private String content;
  @Column(name = "target", nullable = false)
  private int target;
  @Column(name = "current_achievement", columnDefinition = "INTEGER DEFAULT 0")
  private int currentAchievement;
  @Column(name = "unit", nullable = false)
  private String unit;
  @Temporal(TemporalType.DATE)
  @Column(name = "deadline_at", nullable = false)
  private Date deadlineAt;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at")
  private Date createdAt;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_at")
  private Date updatedAt;
  @Column(name = "objective_id", nullable = false)
  private Long objectiveId;

  @PrePersist
  protected void setCreatedAt() {
    this.createdAt = new Date();
    this.updatedAt = new Date();
  }

  @PreUpdate
  protected void setUpdatedAt() {
    this.updatedAt = new Date();
  }
}

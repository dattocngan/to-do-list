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
@Table(name = "objectives")
public class Objective {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "type", nullable = false)
  private String type;
  @Column(name = "content", nullable = false)
  private String content;
  @Column(name = "reason", nullable = false)
  private String reason;
  @Column(name = "status", nullable = false)
  private String status;
  @Temporal(TemporalType.DATE)
  @Column(name = "deadline_at", nullable = false)
  private Date deadlineAt;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at")
  private Date createdAt;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_at")
  private Date updatedAt;
  @Column(name = "user_id", nullable = false)
  private Long userId;

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

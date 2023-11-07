package com.example.todolist.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties(value = {"id", "password", "createdAt", "updatedAt"})
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "username", nullable = false)
  private String username;
  @Column(name = "password", nullable = false)
  private String password;
  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "sex", columnDefinition = "INTEGER DEFAULT 0")
  private int sex;
  @Temporal(TemporalType.DATE)
  @Column(name = "birthday")
  private Date birthday;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at")
  private Date createdAt;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_at")
  private Date updatedAt;

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

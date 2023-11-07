package com.example.todolist.schemas;

import com.example.todolist.models.KeyResult;
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
public class ObjectiveSchema {
  private Long id;
  private String type;
  private String content;
  private String reason;
  private String status;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date deadlineAt;
  private List<KeyResult> keyResults;
}

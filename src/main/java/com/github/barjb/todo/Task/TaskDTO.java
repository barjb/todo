package com.github.barjb.todo.Task;

import java.time.LocalDateTime;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
public class TaskDTO {
  private String description;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;
  private String owner;
}

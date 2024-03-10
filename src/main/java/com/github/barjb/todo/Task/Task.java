package com.github.barjb.todo.Task;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.barjb.todo.User.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "task_id")
  private Long id;

  private String description;
  private String owner;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;

  @ManyToOne()
  @JoinColumn(name = "USER_ID")
  @JsonManagedReference
  private User user;
}

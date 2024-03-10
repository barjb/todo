package com.github.barjb.todo.Task;

public class TaskMapper {
  public static TaskDTO taskToDTO(Task task) {
    return TaskDTO.builder()
        .description(task.getDescription())
        .owner(task.getOwner())
        .createdAt(task.getCreatedAt())
        .updatedAt(task.getUpdatedAt())
        .deletedAt(task.getDeletedAt())
        .build();
  }

  public static Task dtoToTask(TaskDTO taskDTO) {
    return Task.builder()
        .createdAt(taskDTO.getCreatedAt())
        .owner(taskDTO.getOwner())
        .description(taskDTO.getDescription())
        .id(null)
        .deletedAt(null)
        .build();
  }
}

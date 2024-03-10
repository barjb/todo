package com.github.barjb.todo.Task;

import com.github.barjb.todo.Exceptions.BadRequestException;
import com.github.barjb.todo.Exceptions.TaskNotFoundException;
import com.github.barjb.todo.User.User;
import com.github.barjb.todo.User.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
  private final TaskRepository taskRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public List<TaskDTO> findAllByOwner(Pageable pageable, String principal, MyModel model) {
    return taskRepository
        .findAll(
            new TaskSpecificationsBuilder()
                .hasStartDate(model.getStartDate())
                .hasEndDate(model.getEndDate())
                .hasOwner(principal)
                .build(),
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "description"))))
        .getContent()
        .stream()
        .map(TaskMapper::taskToDTO)
        .toList();
  }

  @Override
  @Transactional
  public void putTask(Long id, Task taskUpdate, String principal) {
    Task task = taskRepository.findByIdAndOwner(id, principal);
    if (Objects.isNull(task)) {
      throw new TaskNotFoundException("Task does not exist");
    }
    Task updatedTask =
        Task.builder()
            .id(task.getId())
            .description(taskUpdate.getDescription())
            .owner(principal)
            .createdAt(task.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .deletedAt(null)
            .user(task.getUser())
            .build();
    taskRepository.save(updatedTask);
  }

  @Override
  @Transactional
  public void createTask(Task task, String principal) {
    // Zaciągam usera zeby dać jego id do taska
    // Trzeba dodac credentiale do users i sprawdzic je przed putem i postem
    User user = userRepository.findByUsername(principal);
    if (user == null) throw new BadRequestException("Provided bad user credentials");
    Task newTask =
        Task.builder()
            .description(task.getDescription())
            .owner(principal)
            .createdAt(LocalDateTime.now())
            .user(user)
            .build();
    taskRepository.save(newTask);
  }

  @Override
  @Transactional(readOnly = true)
  public TaskDTO findTaskByIdAndOwner(Long id, String principal) {
    Task task = taskRepository.findByIdAndOwner(id, principal);
    if (Objects.isNull(task)) {
      throw new TaskNotFoundException("Task does not exist");
    }
    return TaskMapper.taskToDTO(task);
  }

  @Override
  @Transactional
  public void deleteTaskById(Long id, String principal) {
    findTaskByIdAndOwner(id, principal);
    taskRepository.deleteTaskByTaskId(id);
  }
}

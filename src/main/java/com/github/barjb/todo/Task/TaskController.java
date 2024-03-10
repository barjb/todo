package com.github.barjb.todo.Task;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
  private final TaskService taskService;

  @GetMapping
  public List<TaskDTO> findByDate(
      @PageableDefault Pageable pageable, @Valid MyModel myModel, Principal principal) {
    return taskService.findAllByOwner(pageable, principal.getName(), myModel);
  }

  @GetMapping("/{id}")
  public TaskDTO findTaskByIdAndOwner(@PathVariable Long id, Principal principal) {
    return taskService.findTaskByIdAndOwner(id, principal.getName());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  private void createTask(@RequestBody Task task, UriComponentsBuilder ucb, Principal principal) {
    taskService.createTask(task, principal.getName());
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void putTask(@PathVariable Long id, @RequestBody Task task, Principal principal) {
    taskService.putTask(id, task, principal.getName());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void deleteTask(@PathVariable Long id, Principal principal) {
    taskService.deleteTaskById(id, principal.getName());
  }
}

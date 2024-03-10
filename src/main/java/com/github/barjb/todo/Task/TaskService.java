package com.github.barjb.todo.Task;

import com.github.barjb.todo.Exceptions.BadRequestException;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TaskService {
  TaskDTO findTaskByIdAndOwner(Long id, String principal);

  List<TaskDTO> findAllByOwner(Pageable pageable, String principal, MyModel model);

  void createTask(Task task, String principal) throws BadRequestException;

  void deleteTaskById(Long id, String principal);

  void putTask(Long id, Task taskUpdate, String principal);
}

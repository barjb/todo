package com.github.barjb.todo.User;

import com.github.barjb.todo.Task.*;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final TaskService taskService;

  @GetMapping("/{id}")
  public UserDTO getUserById(@PathVariable long id, Principal principal) {
    User user = userService.getById(id, principal.getName());
    return UserMapper.userToDTO(user);
  }

  @GetMapping("/{id}/tasks")
  public List<TaskDTO> getUsersTasksById(
      @PathVariable long id, Pageable pageable, Principal principal, @Valid MyModel model) {
    // wywalic info o userze, zwrocic tylko taski, frontas wie info o userze i potrzebuje tylko
    // taski
    userService.getById(id, principal.getName());
    return taskService.findAllByOwner(pageable, principal.getName(), model);
  }
}

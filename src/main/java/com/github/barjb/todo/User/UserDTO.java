package com.github.barjb.todo.User;

import com.github.barjb.todo.Group.GroupDTO;
import com.github.barjb.todo.Task.TaskDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserDTO {
  String email;
  String description;
  String username;
  List<TaskDTO> taskDTOList;
  Set<GroupDTO> groupDTOSet;
  Set<GroupDTO> ownedGroups;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
  LocalDateTime deletedAt;
}

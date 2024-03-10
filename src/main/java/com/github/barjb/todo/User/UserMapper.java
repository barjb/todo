package com.github.barjb.todo.User;

import com.github.barjb.todo.Group.GroupMapper;
import com.github.barjb.todo.Task.TaskMapper;
import java.util.stream.Collectors;

public class UserMapper {
  public static User dtoToUser(UserDTO userDTO) {
    return User.builder()
        .id(null)
        .email(userDTO.getEmail())
        .description(userDTO.getDescription())
        .groups(
            userDTO.getGroupDTOSet().stream()
                .map(GroupMapper::dtoToGroup)
                .collect(Collectors.toSet()))
        .ownedGroups(
            userDTO.getOwnedGroups().stream()
                .map(GroupMapper::dtoToGroup)
                .collect(Collectors.toSet()))
        .createdAt(userDTO.getCreatedAt())
        .deletedAt(userDTO.getDeletedAt())
        .updatedAt(userDTO.getUpdatedAt())
        .build();
  }

  public static UserDTO userToDTO(User user) {
    return UserDTO.builder()
        .email(user.getEmail())
        .username(user.getUsername())
        .description(user.getDescription())
        .groupDTOSet(
            user.getGroups().stream().map(GroupMapper::groupToDTO).collect(Collectors.toSet()))
        .ownedGroups(
            user.getOwnedGroups().stream().map(GroupMapper::groupToDTO).collect(Collectors.toSet()))
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .deletedAt(user.getDeletedAt())
        .taskDTOList(user.getTaskList().stream().map(TaskMapper::taskToDTO).toList())
        .build();
  }
}

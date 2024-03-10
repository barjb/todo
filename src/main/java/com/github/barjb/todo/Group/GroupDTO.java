package com.github.barjb.todo.Group;

import com.github.barjb.todo.User.User;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GroupDTO {
  private String description;
  private User owner;
  private Set<User> members;
}

package com.github.barjb.todo.Group;

import java.util.List;

public interface GroupService {
  List<GroupDTO> findAll();

  GroupDTO findById(long id);

  void createGroup(Group group, String username);

  void putGroup(long id, Group group, String principal);

  void deleteGroup(long id);
}

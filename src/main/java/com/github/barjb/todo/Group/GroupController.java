package com.github.barjb.todo.Group;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {
  private final GroupService groupService;

  @GetMapping
  public List<GroupDTO> findGroups(Principal principal) {
    return groupService.findAll();
  }

  @GetMapping("/{id}")
  public GroupDTO findGroupById(@PathVariable Long id, Principal principal) {
    return groupService.findById(id);
  }
}

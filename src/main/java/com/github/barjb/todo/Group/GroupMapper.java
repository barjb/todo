package com.github.barjb.todo.Group;

public class GroupMapper {
  public static GroupDTO groupToDTO(Group group) {
    return GroupDTO.builder()
        .description(group.getDescription())
        .owner(group.getOwner())
        .members(group.getMembers())
        .build();
  }

  public static Group dtoToGroup(GroupDTO groupDTO) {
    return Group.builder()
        .description(groupDTO.getDescription())
        .owner(groupDTO.getOwner())
        .members(groupDTO.getMembers())
        .build();
  }
}

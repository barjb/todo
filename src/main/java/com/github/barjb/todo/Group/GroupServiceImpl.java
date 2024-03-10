package com.github.barjb.todo.Group;

import com.github.barjb.todo.Exceptions.GroupNotFoundException;
import com.github.barjb.todo.User.User;
import com.github.barjb.todo.User.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
  private final GroupRepository groupRepository;
  private final UserRepository userRepository;

  public List<GroupDTO> findAll() {
    return groupRepository.findAll().stream().map(GroupMapper::groupToDTO).toList();
  }

  @Override
  public GroupDTO findById(long id) {
    return GroupMapper.groupToDTO(
        groupRepository
            .findById(id)
            .orElseThrow(() -> new GroupNotFoundException("Not found group with given id")));
  }

  @Override
  public void createGroup(Group group, String username) {
    User user = userRepository.findByUsername(username);
    group.setOwner(user);
    groupRepository.save(group);
  }

  @Override
  public void putGroup(long id, Group groupUpdate, String principal) {
    User user = userRepository.findByUsername(principal);
    Group group =
        groupRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new GroupNotFoundException(
                        String.format("Can't find group with given id %d", id)));
    group.setDescription(groupUpdate.getDescription());
    groupRepository.save(group);
  }

  @Override
  public void deleteGroup(long id) {
    Group group =
        groupRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new GroupNotFoundException(
                        String.format("Can't find group with given id %d", id)));
    groupRepository.delete(group);
  }
}

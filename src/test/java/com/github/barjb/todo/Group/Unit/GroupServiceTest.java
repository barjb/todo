package com.github.barjb.todo.Group.Unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.barjb.todo.Exceptions.GroupNotFoundException;
import com.github.barjb.todo.Group.Group;
import com.github.barjb.todo.Group.GroupRepository;
import com.github.barjb.todo.Group.GroupService;
import com.github.barjb.todo.Group.GroupServiceImpl;
import com.github.barjb.todo.User.User;
import com.github.barjb.todo.User.UserRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupServiceTest {
  private GroupService groupService;
  private GroupRepository groupRepository = mock(GroupRepository.class);
  private UserRepository userRepository = mock(UserRepository.class);

  @BeforeEach
  void setUp() {
    groupService = new GroupServiceImpl(groupRepository, userRepository);
  }

  @Test
  void shouldFindAllGroups() {
    groupService.findAll();
    verify(groupRepository, times(1)).findAll();
  }

  @Test
  void shouldFindGroupById() {
    long id = 1;
    given(groupRepository.findById(id)).willReturn(Optional.ofNullable(Group.builder().build()));
    groupService.findById(id);
    verify(groupRepository, times(1)).findById(id);
  }

  @Test
  void canCreateGroup() {
    // given
    User user = User.builder().build();
    Group group = Group.builder().description("group").owner(user).build();
    // when
    groupService.createGroup(group, user.getUsername());
    // then
    verify(groupRepository, times(1)).save(group);
    verify(userRepository, times(1)).findByUsername(user.getUsername());
  }

  @Test
  void canPutExistingGroup() {
    // given
    String principal = "qqq";
    long id = 1;
    User user = User.builder().build();
    Group group = Group.builder().description("group").owner(user).build();
    // when
    given(groupRepository.findById(id)).willReturn(Optional.ofNullable(group));
    groupService.putGroup(id, group, principal);
    // then
    verify(userRepository, times(1)).findByUsername(principal);
    verify(groupRepository, times(1)).save(group);
  }

  @Test
  void willThrowWhenPuttingNonExistingGroup() {
    String principal = "qqq";
    long id = 14;
    User user = User.builder().build();
    Group group = Group.builder().description("group").owner(user).build();
    // then
    Assertions.assertThatExceptionOfType(GroupNotFoundException.class)
        .isThrownBy(
            () -> {
              groupService.putGroup(id, group, principal);
            });
  }

  @Test
  void canDeleteExistingGroup() {
    long id = 1;
    Group group = Group.builder().build();
    given(groupRepository.findById(id)).willReturn(Optional.ofNullable(group));
    groupService.deleteGroup(id);
    verify(groupRepository, times(1)).delete(group);
  }

  @Test
  void cantDeleteNonExistingGroup() {
    long id = 1;
    given(groupRepository.findById(id)).willReturn(Optional.ofNullable(null));
    Assertions.assertThatExceptionOfType(GroupNotFoundException.class)
        .isThrownBy(
            () -> {
              groupService.deleteGroup(id);
            });
  }
}

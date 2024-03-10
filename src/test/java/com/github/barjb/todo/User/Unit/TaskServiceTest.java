package com.github.barjb.todo.User.Unit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.barjb.todo.Exceptions.BadRequestException;
import com.github.barjb.todo.Exceptions.TaskNotFoundException;
import com.github.barjb.todo.Task.MyModel;
import com.github.barjb.todo.Task.Task;
import com.github.barjb.todo.Task.TaskRepository;
import com.github.barjb.todo.Task.TaskService;
import com.github.barjb.todo.Task.TaskServiceImpl;
import com.github.barjb.todo.User.User;
import com.github.barjb.todo.User.UserRepository;
import java.time.LocalDateTime;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class TaskServiceTest {
  private TaskRepository taskRepository = mock(TaskRepository.class);
  private UserRepository userRepository = mock(UserRepository.class);
  private TaskService taskService;

  @BeforeEach
  void setUp() {
    // before each test we get new instance of taskService
    taskService = new TaskServiceImpl(taskRepository, userRepository);
  }

  @Test
  void canFindAllByOwner() {
    // given
    String principal = "qqq";
    MyModel model =
        MyModel.builder()
            .startDate(LocalDateTime.of(2024, 1, 1, 1, 1))
            .endDate(LocalDateTime.of(2024, 1, 1, 1, 2))
            .build();
    PageRequest pageRequest = mock(PageRequest.class);

    //    PageRequest pageRequest = PageRequest.of(1, 1, Sort.by(Sort.Direction.ASC,
    // "description"));
    // when
    when(pageRequest.getPageSize()).thenReturn(1);
    when(pageRequest.getPageNumber()).thenReturn(1);
    when(pageRequest.getSort()).thenReturn(Sort.by(Sort.Order.asc("description")));

    taskService.findAllByOwner(pageRequest, principal, model);
    // then
    verify(taskRepository, times(1)).findByOwner(Mockito.anyString(), any(PageRequest.class));
  }

  @Test
  void canFindByIdAndOwner() {
    // given
    String principal = "qqq";
    Long id = 1l;
    Task task = Task.builder().id(id).owner(principal).build();
    // 1 sposob
    when(taskRepository.findByIdAndOwner(id, principal)).thenReturn(new Task());
    // when
    taskService.findTaskByIdAndOwner(id, principal);
    //     then
    verify(taskRepository, times(1)).findByIdAndOwner(id, principal);

    //     2 sposob
    //    when(taskRepository.findByIdAndOwner(id, principal)).thenReturn(task);
    //     when
    //    var returned = taskService.findTaskByIdAndOwner(id, principal);
    //     then
    //
    //    Assertions.assertThat(returned).satisfies(ret -> {
    //      assertThat(ret.getOwner()).isEqualTo(task.getOwner());
    //      assertThat(ret.getId()).isEqualTo(task.getId());
    //    });
  }

  @Test
  void canSaveTask() {
    // given
    String principal = "testUser";
    Task task =
        Task.builder()
            .id(null)
            .owner(principal)
            .description("zzz")
            .createdAt(null)
            .updatedAt(null)
            .deletedAt(null)
            .build();
    // when
    when(userRepository.findByUsername(principal))
        .thenReturn(User.builder().id(1l).username(principal).build());
    taskService.createTask(task, principal);
    // then
    ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
    verify(taskRepository).save(taskArgumentCaptor.capture());
    Task capturedTask = taskArgumentCaptor.getValue();

    Assertions.assertThat(capturedTask)
        .satisfies(
            t -> {
              assertThat(t.getOwner()).isEqualTo(task.getOwner());
              assertThat(t.getId()).isEqualTo(task.getId());
            });
  }

  @Test
  @SneakyThrows
  void willThrowWhenTaskIsRepeated() {
    // given
    String principal = "testUser";
    Task task =
        Task.builder()
            .id(null)
            .owner(principal)
            .description("zzz")
            .createdAt(LocalDateTime.now())
            .updatedAt(null)
            .deletedAt(null)
            .build();
    //    given(taskRepository.selectExistTask("zzz", principal)).willReturn(true);
    // when
    org.junit.jupiter.api.Assertions.assertThrows(
        BadRequestException.class,
        () -> {
          taskService.createTask(task, principal);
        });
    // then
    verify(taskRepository, never()).save(any());
  }

  @Test
  void canPutExistingTask() {
    long id = 1;
    String principal = "www";
    Task task = Task.builder().id(id).description("ok").owner(principal).build();
    given(taskRepository.findByIdAndOwner(id, principal)).willReturn(task);
    taskService.putTask(id, task, principal);
    Assertions.assertThat(taskRepository.findByIdAndOwner(id, principal)).isEqualTo(task);
  }

  @Test
  void willThrowWhenPuttingNonExistingTask() {
    // given
    long id = 1;
    String principal = "test";
    Task task = Task.builder().id(id).description("ok").owner(principal).build();
    Assertions.assertThatExceptionOfType(TaskNotFoundException.class)
        .isThrownBy(
            () -> {
              taskService.putTask(id, task, principal);
            })
        .withMessage("Task does not exist");
  }
}

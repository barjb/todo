package com.github.barjb.todo.Task.Unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.barjb.todo.Task.Task;
import com.github.barjb.todo.Task.TaskRepository;
import jakarta.persistence.EntityManager;
import java.util.stream.StreamSupport;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest
class TaskRepositoryTest {
  @Autowired private TaskRepository taskRepository;
  @Autowired private DataSource dataSource;
  @Autowired private EntityManager entityManager;

  @Test
  void testInit() {
    Assertions.assertThat(
            StreamSupport.stream(taskRepository.findAll().spliterator(), false).count())
        .isEqualTo(13);
    Assertions.assertThat(dataSource).isNotNull();
    Assertions.assertThat(entityManager).isNotNull();
  }

  @Test
  void shouldFindTaskWhenItExistsByIdAndOwner() {
    // given
    long id = 1l;
    String principal = "qqq";
    // when
    Task task = taskRepository.findByIdAndOwner(id, principal);
    // then
    Assertions.assertThat(task).isNotNull();
    Assertions.assertThat(task.getId()).isEqualTo(id);
    Assertions.assertThat(task.getOwner()).isEqualTo(principal);
  }

  @Test
  void shouldNotFindTaskWhenItDoesNotExistByIdAndOwner() {
    // given
    long id = 1l;
    String principal = "aaa";
    // when
    Task task = taskRepository.findByIdAndOwner(id, principal);
    // then
    Assertions.assertThat(task).isNull();
  }

  @Test
  void shouldReturnTasksWhenTheyExistsByOwner() {
    // given
    String principal = "qqq";
    PageRequest pageRequestMock = mock(PageRequest.class);
    // when
    when(pageRequestMock.getPageNumber()).thenReturn(0);
    when(pageRequestMock.getPageSize()).thenReturn(10);
    when(pageRequestMock.getSort()).thenReturn(Sort.by(Sort.Order.asc("id")));
    Page<Task> tasks = taskRepository.findByOwner(principal, pageRequestMock);
    // then
    Assertions.assertThat(tasks).isNotNull();
  }

  @Test
  void shouldNotReturnTasksWhenTheyDoNotExistByOwner() {
    // given
    String principal = "nonexistinguser";
    PageRequest pageRequestMock = mock(PageRequest.class);
    // when
    when(pageRequestMock.getPageNumber()).thenReturn(0);
    when(pageRequestMock.getPageSize()).thenReturn(10);
    when(pageRequestMock.getSort()).thenReturn(Sort.by(Sort.Order.asc("id")));
    Page<Task> tasks = taskRepository.findByOwner(principal, pageRequestMock);
    // then
    Assertions.assertThat(StreamSupport.stream(tasks.spliterator(), false).count()).isEqualTo(0);
  }

  @Test
  void shouldDeleteTaskByIdWhenItExists() {
    // given
    long id = 1l;
    String owner = "qqq";
    // when
    taskRepository.deleteTaskByTaskId(id);
    // then
    Assertions.assertThat(taskRepository.findByIdAndOwner(id, owner)).isNull();
  }

  @Test
  void shouldNotDeleteTaskByIdWhenItDoesNotExist() {}
}

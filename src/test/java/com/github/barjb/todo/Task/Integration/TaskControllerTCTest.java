package com.github.barjb.todo.Task.Integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.barjb.todo.Task.Task;
import com.github.barjb.todo.User.User;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import java.net.URI;
import java.time.LocalDateTime;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@IntegrationTest
@ContextConfiguration(classes = TaskControllerTCTest.TestConfig.class)
public class TaskControllerTCTest {
  @TestConfiguration
  static class TestConfig {}

  @Autowired TestRestTemplate restTemplate;

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldReturnTodo() {
    // given
    ResponseEntity<String> response =
        restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks/1", String.class);
    // when
    DocumentContext documentContext = JsonPath.parse(response.getBody());
    // then
    String description = documentContext.read("$.description");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat("API").isEqualTo(description);
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldNotReturnATaskWhenUsingBadCredentials() {
    // given
    // when
    ResponseEntity<String> response =
        restTemplate.withBasicAuth("qqq", "111111").getForEntity("/api/v1/tasks", String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    // when
    response =
        restTemplate
            .withBasicAuth("notauser", "abc123")
            .getForEntity("/api/v1/tasks", String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldRejectUsersWhoAreNotTaskOwners() {
    // given
    // when
    ResponseEntity<String> response =
        restTemplate.withBasicAuth("zzz", "zzz").getForEntity("/api/v1/tasks/1", String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldNotAllowAccessToTasksTheyDoNotOwn() {
    // given
    // when
    ResponseEntity<String> response =
        restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks/5", String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldReturnTodoListWhenListIsRequested() {
    // given
    // when
    ResponseEntity<String> response =
        restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks", String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    DocumentContext documentContext = JsonPath.parse(response.getBody());
    JSONArray jsonArray = documentContext.read("$");
    assertThat(jsonArray.size()).isEqualTo(5);
    JSONArray descriptions = documentContext.read("$..description");
    assertThat(descriptions)
        .containsExactlyInAnyOrder("API", "PostgreSQL", "REACT CLONE", "random", "shopping");
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldReturnAPageOfTasks() {
    ResponseEntity<String> response =
        restTemplate
            .withBasicAuth("qqq", "qqq")
            .getForEntity("/api/v1/tasks?page=0&size=1", String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    DocumentContext documentContext = JsonPath.parse(response.getBody());
    JSONArray jsonArray = documentContext.read("$");
    assertThat(jsonArray.size()).isEqualTo(1);
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldReturnASortedPageOfTasks() {
    ResponseEntity<String> response =
        restTemplate
            .withBasicAuth("qqq", "qqq")
            .getForEntity("/api/v1/tasks?page=0&size=1&sort=id,desc", String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    DocumentContext documentContext = JsonPath.parse(response.getBody());
    // SPRAWDZIC
    JSONArray jsonArray = documentContext.read("$");
    assertThat(jsonArray.size()).isEqualTo(1);
    JSONArray description = documentContext.read("$..description");
    assertThat(description).containsExactlyInAnyOrder("PostgreSQL");
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldReturnASortedPageOfTasksWithNoParametersAndUseDefaultValues() {
    ResponseEntity<String> response =
        restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks", String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    DocumentContext documentContext = JsonPath.parse(response.getBody());
    JSONArray jsonArray = documentContext.read("$");
    assertThat(jsonArray.size()).isEqualTo(5);
    JSONArray descriptions = documentContext.read("$..description");
    assertThat(descriptions)
        .containsExactlyInAnyOrder("API", "PostgreSQL", "REACT CLONE", "random", "shopping");
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldNotReturnATaskWithUnknownId() {
    ResponseEntity<String> response =
        restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks/1000", String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldCreateANewTask() {
    Task task =
        Task.builder()
            .description("Very new task")
            .owner(null)
            .createdAt(LocalDateTime.now())
            .deletedAt(null)
            .updatedAt(null)
            .user(User.builder().id(1L).build())
            .build();
    ResponseEntity<Void> response =
        restTemplate.withBasicAuth("qqq", "qqq").postForEntity("/api/v1/tasks", task, Void.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    URI location = response.getHeaders().getLocation();
    ResponseEntity<String> getResponse =
        restTemplate.withBasicAuth("qqq", "qqq").getForEntity(location, String.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
    String description = documentContext.read("$.description");
    assertThat("Very new task").isEqualTo(description);
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldUpdateAnExistingTask() {
    Task task =
        Task.builder()
            .id(null)
            .description("new task zzzzzz")
            .owner(null)
            .createdAt(LocalDateTime.now())
            .updatedAt(null)
            .deletedAt(null)
            .user(User.builder().id(1L).build())
            .build();
    HttpEntity<Task> request = new HttpEntity<>(task);
    ResponseEntity<Void> response =
        restTemplate
            .withBasicAuth("qqq", "qqq")
            .exchange("/api/v1/tasks/1", HttpMethod.PUT, request, Void.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    ResponseEntity<String> getResponse =
        restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks/1", String.class);
    DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
    String description = documentContext.read("$.description");
    assertThat("new task zzzzzz").isEqualTo(description);
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldNotUpdateCardThatDoesNotExist() {
    Task task =
        Task.builder()
            .id(null)
            .description("new task zzzzzz")
            .owner(null)
            .createdAt(LocalDateTime.now())
            .updatedAt(null)
            .deletedAt(null)
            .user(User.builder().id(1L).build())
            .build();
    HttpEntity<Task> request = new HttpEntity<>(task);
    ResponseEntity<Void> response =
        restTemplate
            .withBasicAuth("qqq", "qqq")
            .exchange("/api/v1/tasks/13232323", HttpMethod.PUT, request, Void.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldDeleteAnExistingTask() {
    ResponseEntity<Void> response =
        restTemplate
            .withBasicAuth("qqq", "qqq")
            .exchange("/api/v1/tasks/1", HttpMethod.DELETE, null, Void.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    ResponseEntity<String> getResponse =
        restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks/1", String.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldNotDeleteAnTaskThatDoesNotExist() {
    ResponseEntity<Void> response =
        restTemplate
            .withBasicAuth("qqq", "qqq")
            .exchange("/api/v1/tasks/32323232", HttpMethod.DELETE, null, Void.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldNotDeleteAnTaskThatTheyDoNotOwn() {
    ResponseEntity<Void> response =
        restTemplate
            .withBasicAuth("aaa", "aaa")
            .exchange("/api/v1/tasks/1", HttpMethod.DELETE, null, Void.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    ResponseEntity<String> getResponse =
        restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks/1", String.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}

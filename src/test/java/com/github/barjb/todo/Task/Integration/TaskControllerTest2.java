package com.github.barjb.todo.Task.Integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TaskControllerTest2 {
  @Autowired MockMvc mockMvc;

  @Test
  @WithMockUser(
      username = "qqq",
      password = "qqq",
      roles = {"ROLE-USER", "ROLE-ADMIN"})
  @SneakyThrows
  void shouldReturnTodoWhenDataIsSaved() {
    mockMvc
        .perform(get("/api/v1/tasks/{id}", "1").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.description", Matchers.is("API")));
  }

  @Test
  @WithMockUser(
      username = "qqq",
      password = "111",
      roles = {"ROLE-ADMIN"})
  @SneakyThrows
  void shouldNotReturnTasksWhenNotHavingUserRole() {
    // obecnie ROLE-USER przepuszcza
    mockMvc
        .perform(get("/api/v1/tasks").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(
      username = "zzz",
      password = "zzz",
      roles = {"ROLE-USER", "NON-OWNER"})
  @SneakyThrows
  void shouldRejectUsersWhoAreNotTaskOwners() {
    mockMvc.perform(get("/api/v1/tasks/{id}", "1")).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(
      username = "qqq",
      password = "qqq",
      roles = {"ROLE-USER", "CARD-OWNER"})
  @SneakyThrows
  void shouldNotAllowAccessToTasksTheyDoNotOwn() {
    mockMvc.perform(get("/api/v1/tasks/{id}", "5")).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(
      username = "qqq",
      password = "qqq",
      roles = {"ROLE-USER", "CARD-OWNER"})
  @SneakyThrows
  void shouldReturnTodoListWhenListIsRequested() {
    mockMvc
        .perform(get("/api/v1/tasks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$..description").isArray())
        .andExpect(jsonPath("$..description", Matchers.hasSize(5)))
        .andExpect(
            jsonPath(
                "$..description",
                Matchers.containsInAnyOrder(
                    "API", "PostgreSQL", "REACT CLONE", "random", "shopping")));
  }

  @Test
  @WithMockUser(
      username = "qqq",
      password = "qqq",
      roles = {"ROLE-USER", "CARD-OWNER"})
  @SneakyThrows
  void shouldReturnAPageOfTasks() {
    mockMvc
        .perform(get("/api/v1/tasks?page={page}&size={size}", "0", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(1)));
  }

  @Test
  @WithMockUser(
      username = "qqq",
      password = "qqq",
      roles = {"ROLE-USER", "CARD-OWNER"})
  @SneakyThrows
  void shouldReturnASortedPageOfTasks() {
    mockMvc
        .perform(get("/api/v1/tasks?page=0&size=1&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(1)))
        .andExpect(jsonPath("$..description", Matchers.containsInAnyOrder("PostgreSQL")));
  }

  @Test
  @WithMockUser(
      username = "qqq",
      password = "qqq",
      roles = {"ROLE-USER", "CARD-OWNER"})
  @SneakyThrows
  void shouldReturnASortedPageOfTasksWithNoParametersAndUseDefaultValues() {
    //      ResponseEntity<String> response =
    //          restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks",
    // String.class);
    //      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    //
    //      DocumentContext documentContext = JsonPath.parse(response.getBody());
    //      JSONArray jsonArray = documentContext.read("$");
    //      assertThat(jsonArray.size()).isEqualTo(5);
    //      JSONArray descriptions = documentContext.read("$..description");
    //      assertThat(descriptions)
    //          .containsExactlyInAnyOrder("API", "PostgreSQL", "REACT CLONE", "random",
    // "shopping");
    mockMvc
        .perform(get("/api/v1/tasks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(5)))
        .andExpect(
            jsonPath(
                "$..description",
                Matchers.containsInAnyOrder(
                    "API", "PostgreSQL", "REACT CLONE", "random", "shopping")));
  }

  @Test
  @WithMockUser(
      username = "qqq",
      password = "qqq",
      roles = {"ROLE-USER", "CARD-OWNER"})
  @SneakyThrows
  void shouldNotReturnATaskWithUnknownId() {
    //      ResponseEntity<String> response =
    //          restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks/1000",
    //   String.class);
    //      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    mockMvc.perform(get("/api/v1/tasks/1000")).andExpect(status().isNotFound());
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  //  @Test
  //  @WithMockUser(
  //          username = "qqq",
  //          password = "qqq",
  //          roles = {"ROLE-USER", "CARD-OWNER"})
  //  @SneakyThrows
  //    @DirtiesContext
  //    void shouldCreateANewTask() {
  //      Task task =
  //          Task.builder()
  //              .description("Very new task")
  //              .owner(null)
  //              .createdAt(LocalDateTime.now())
  //              .deletedAt(null)
  //              .updatedAt(null)
  //              .user(User.builder().id(1L).build())
  //              .build();
  //    mockMvc
  //        .perform(
  //            post("/api/v1/tasks")
  //                .content(asJsonString(task))
  //                .contentType(MediaType.APPLICATION_JSON))
  //        .andExpect(status().isCreated());
  //    ResponseEntity<Void> response =
  //        restTemplate.withBasicAuth("qqq", "qqq").postForEntity("/api/v1/tasks", task,
  // Void.class);
  //    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  //
  //    URI location = response.getHeaders().getLocation();
  //    ResponseEntity<String> getResponse =
  //        restTemplate.withBasicAuth("qqq", "qqq").getForEntity(location, String.class);
  //    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
  //    DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
  //    String description = documentContext.read("$.description");
  //    assertThat("Very new task").isEqualTo(description);
  //    mockMvc.perform(post("/api/v1/tasks").content())
  //    }
  //
  //  @Test
  //  void shouldUpdateAnExistingTask() {
  //    Task task =
  //        Task.builder()
  //            .id(null)
  //            .description("new task zzzzzz")
  //            .owner(null)
  //            .createdAt(LocalDateTime.now())
  //            .updatedAt(null)
  //            .deletedAt(null)
  //            .user(User.builder().id(1L).build())
  //            .build();
  //    HttpEntity<Task> request = new HttpEntity<>(task);
  //    ResponseEntity<Void> response =
  //        restTemplate
  //            .withBasicAuth("qqq", "qqq")
  //            .exchange("/api/v1/tasks/1", HttpMethod.PUT, request, Void.class);
  //    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  //
  //    ResponseEntity<String> getResponse =
  //        restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks/1", String.class);
  //    DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
  //    String description = documentContext.read("$.description");
  //    assertThat("new task zzzzzz").isEqualTo(description);
  //  }
  //
  //  @Test
  //  void shouldNotUpdateCardThatDoesNotExist() {
  //    Task task =
  //        Task.builder()
  //            .id(null)
  //            .description("new task zzzzzz")
  //            .owner(null)
  //            .createdAt(LocalDateTime.now())
  //            .updatedAt(null)
  //            .deletedAt(null)
  //            .user(User.builder().id(1L).build())
  //            .build();
  //    HttpEntity<Task> request = new HttpEntity<>(task);
  //    ResponseEntity<Void> response =
  //        restTemplate
  //            .withBasicAuth("qqq", "qqq")
  //            .exchange("/api/v1/tasks/13232323", HttpMethod.PUT, request, Void.class);
  //    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  //  }
  //
  //  @Test
  //  void shouldDeleteAnExistingTask() {
  //    ResponseEntity<Void> response =
  //        restTemplate
  //            .withBasicAuth("qqq", "qqq")
  //            .exchange("/api/v1/tasks/1", HttpMethod.DELETE, null, Void.class);
  //    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  //
  //    ResponseEntity<String> getResponse =
  //        restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks/1", String.class);
  //    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  //  }
  //
  //  @Test
  //  void shouldNotDeleteAnTaskThatDoesNotExist() {
  //    ResponseEntity<Void> response =
  //        restTemplate
  //            .withBasicAuth("qqq", "qqq")
  //            .exchange("/api/v1/tasks/32323232", HttpMethod.DELETE, null, Void.class);
  //    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  //  }
  //
  //  @Test
  //  void shouldNotDeleteAnTaskThatTheyDoNotOwn() {
  //    ResponseEntity<Void> response =
  //        restTemplate
  //            .withBasicAuth("aaa", "aaa")
  //            .exchange("/api/v1/tasks/1", HttpMethod.DELETE, null, Void.class);
  //    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  //
  //    ResponseEntity<String> getResponse =
  //        restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/tasks/1", String.class);
  //    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
  //  }
}

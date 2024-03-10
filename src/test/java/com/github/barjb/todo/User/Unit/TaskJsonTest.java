package com.github.barjb.todo.User.Unit;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.barjb.todo.Task.Task;
import com.github.barjb.todo.User.User;
import java.io.IOException;
import java.time.LocalDateTime;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class TaskJsonTest {
  @Autowired private JacksonTester<Task> json;
  @Autowired private JacksonTester<Task[]> jsonList;
  private Task[] tasks;

  @BeforeEach
  void setUp() {
    tasks =
        Arrays.array(
            Task.builder()
                .id(1L)
                .description("API")
                .owner("john1")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .user(User.builder().id(1L).build())
                .build(),
            Task.builder()
                .id(2L)
                .description("REACT CLONE")
                .owner("john1")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .user(User.builder().id(1L).build())
                .build(),
            Task.builder()
                .id(3L)
                .description("Homework")
                .owner("john1")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .user(User.builder().id(1L).build())
                .build(),
            Task.builder()
                .id(4L)
                .description("shopping")
                .owner("john1")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .user(User.builder().id(1L).build())
                .build(),
            Task.builder()
                .id(5L)
                .description("stuff")
                .owner("matty")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .user(User.builder().id(1L).build())
                .build(),
            Task.builder()
                .id(6L)
                .description("random")
                .owner("matty")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .user(User.builder().id(1L).build())
                .build());
  }

  @Test
  void taskListSerializationTest() throws IOException {
    assertThat(jsonList.write(tasks)).isStrictlyEqualToJson("list.json");
  }

  @Test
  void taskListDeserializationTest() throws IOException {
    String expected =
        """
                [
                  { "id": 1, "description": "API" },
                  { "id": 2, "description": "REACT CLONE" },
                  { "id": 3, "description": "Homework" },
                  { "id": 4, "description": "shopping" },
                  { "id": 5, "description": "stuff" },
                  { "id": 6, "description": "random" }
                ]
                """;
    assertThat(jsonList.parse(expected)).isEqualTo(tasks);
  }
}

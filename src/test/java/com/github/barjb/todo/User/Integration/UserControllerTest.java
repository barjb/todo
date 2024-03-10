package com.github.barjb.todo.User.Integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@IntegrationTest
@ContextConfiguration(classes = UserControllerTest.TestConfig.class)
public class UserControllerTest {
  @TestConfiguration
  static class TestConfig {}

  @Autowired TestRestTemplate restTemplate;

  @Test
  @Sql(scripts = {"classpath:cleanup.sql", "classpath:init.sql"})
  void shouldReturnTodo() {
    // given
    ResponseEntity<String> response =
        restTemplate.withBasicAuth("qqq", "qqq").getForEntity("/api/v1/users/1", String.class);
    // when
    DocumentContext documentContext = JsonPath.parse(response.getBody());
    // then
    String description = documentContext.read("$.description");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat("API").isEqualTo(description);
  }
}

package com.github.barjb.todo.Task;

import com.github.barjb.todo.User.User;
import jakarta.persistence.criteria.Join;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecificationsBuilder {
  private Specification<Task> spec;

  public TaskSpecificationsBuilder() {
    spec = Specification.where(null);
  }

  public TaskSpecificationsBuilder hasOwner(String principal) {
    if (Objects.nonNull(principal)) {
      spec =
          spec.and(
              (root, query, cb) -> {
                Join<Task, User> userJoin = root.join("user");
                return cb.equal(userJoin.get("username"), principal);
              });
    }
    return this;
  }

  public TaskSpecificationsBuilder hasStartDate(LocalDateTime start) {
    if (Objects.nonNull(start)) {
      spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("createdAt"), start));
    }
    return this;
  }

  public TaskSpecificationsBuilder hasEndDate(LocalDateTime end) {
    if (Objects.nonNull(end)) {
      spec = spec.and((root, query, cb) -> cb.lessThan(root.get("createdAt"), end));
    }
    return this;
  }

  public Specification<Task> build() {
    return spec;
  }
}

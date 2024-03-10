package com.github.barjb.todo.User;

public interface UserService {
  User getById(Long id, String principal);

  User existsByUsername(String principal);
}

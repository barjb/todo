package com.github.barjb.todo.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository
    extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
  User findByUsername(String username);
}

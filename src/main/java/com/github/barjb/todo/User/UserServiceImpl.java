package com.github.barjb.todo.User;

import com.github.barjb.todo.Exceptions.UserNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public User getById(Long id, String principal) {
    return userRepository
        .findById(id)
        .orElseThrow(
            () -> new UserNotFoundException(String.format("User Not Found with given id %d", id)));
  }

  @Override
  @Transactional(readOnly = true)
  public User existsByUsername(String principal) {

    User user = userRepository.findByUsername(principal);
    return Optional.ofNullable(user)
        .orElseThrow(
            () ->
                new UserNotFoundException(
                    String.format("User Not Found with given username %s", principal)));
  }
}

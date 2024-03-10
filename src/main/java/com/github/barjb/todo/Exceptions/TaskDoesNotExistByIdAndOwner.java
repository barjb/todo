package com.github.barjb.todo.Exceptions;

public class TaskDoesNotExistByIdAndOwner extends RuntimeException {
  public TaskDoesNotExistByIdAndOwner(String message) {
    super(message);
  }
}

package com.github.barjb.todo.Exceptions;

public class BadRequestException extends RuntimeException {
  public BadRequestException(String taskAlreadyExists) {
    super(taskAlreadyExists);
  }
}

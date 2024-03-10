package com.github.barjb.todo;

import com.github.barjb.todo.Exceptions.*;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CustomError> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    HttpHeaders headers = new HttpHeaders();
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ex.getBindingResult().getAllErrors().stream().forEach(System.out::println);
    CustomError error =
        CustomError.builder()
            .reason("Validation error")
            .messages(
                ex.getBindingResult().getAllErrors().stream()
                    .map(
                        objectError -> {
                          if (Objects.nonNull(objectError.getCodes())) {
                            return Arrays.stream(objectError.getCodes())
                                    .map(code -> code.substring(code.lastIndexOf(".") + 1))
                                    .findFirst()
                                    .orElse("Error finding field name")
                                + " "
                                + objectError.getDefaultMessage();
                          }
                          return "Error in processing codes";
                        })
                    .toList())
            .build();
    return new ResponseEntity<>(error, headers, status);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
    HttpHeaders headers = new HttpHeaders();
    HttpStatus httpStatus = HttpStatus.OK;
    return new ResponseEntity<>(
        "BadRequestException error: " + e.getMessage(), headers, httpStatus);
  }

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException e) {
    HttpHeaders headers = new HttpHeaders();
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    return new ResponseEntity<>("Task not found: " + e.getMessage(), headers, httpStatus);
  }

  @ExceptionHandler(TaskDoesNotExistByIdAndOwner.class)
  public ResponseEntity<String> handleTaskDoesNotExistByIdAndOwnerException(
      TaskDoesNotExistByIdAndOwner e) {
    HttpHeaders headers = new HttpHeaders();
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    return new ResponseEntity<>("Task not found: " + e.getMessage(), headers, httpStatus);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> userNotFoundException(UserNotFoundException e) {
    HttpHeaders headers = new HttpHeaders();
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    return new ResponseEntity<>("User not found: " + e.getMessage(), headers, httpStatus);
  }

  @ExceptionHandler(GroupNotFoundException.class)
  public ResponseEntity<String> groupNotFoundException(GroupNotFoundException e) {
    HttpHeaders headers = new HttpHeaders();
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    return new ResponseEntity<>("Group not found: " + e.getMessage(), headers, httpStatus);
  }
}

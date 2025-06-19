package me.vladislav.homework.app.core.exception;

import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.core.exception.db.repository.BookNotFoundException;
import me.vladislav.homework.app.core.exception.db.repository.CourseNotFoundException;
import me.vladislav.homework.app.core.exception.db.repository.UniversityNotFoundException;
import me.vladislav.homework.app.core.exception.db.repository.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class MainExceptionHandler {
  @ExceptionHandler
  public ResponseEntity<String> handle(Exception e) {
    log.error(e.getMessage());
    return ResponseEntity.internalServerError().body(e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handle(UserNotFoundException e) {
    log.warn(e.getMessage());
    return ResponseEntity.status(404).body(e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handle(BookNotFoundException e) {
    log.warn(e.getMessage());
    return ResponseEntity.status(404).body(e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handle(CourseNotFoundException e) {
    log.warn(e.getMessage());
    return ResponseEntity.status(404).body(e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handle(UniversityNotFoundException e) {
    log.warn(e.getMessage());
    return ResponseEntity.status(404).body(e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException e) {
    log.error(e.getMessage());
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return ResponseEntity.status(422).body(errors);
  }
}

package me.vladislav.homework01.app.core.exception;

import me.vladislav.homework01.app.core.exception.db.repository.CourseNotFoundException;
import me.vladislav.homework01.app.core.exception.db.repository.UniversityNotFoundException;
import me.vladislav.homework01.app.core.exception.db.repository.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MainExceptionHandler {
  @ExceptionHandler
  public ResponseEntity<String> handle(Exception e) {
    return ResponseEntity.internalServerError().body(e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handle(UserNotFoundException e) {
    return ResponseEntity.status(404).body(e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handle(CourseNotFoundException e) {
    return ResponseEntity.status(404).body(e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> handle(UniversityNotFoundException e) {
    return ResponseEntity.status(404).body(e.getMessage());
  }
}

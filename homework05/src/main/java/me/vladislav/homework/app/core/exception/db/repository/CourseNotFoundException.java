package me.vladislav.homework.app.core.exception.db.repository;

public class CourseNotFoundException extends RuntimeException {
  public CourseNotFoundException() {
    super("Requested course not found.");
  }
}


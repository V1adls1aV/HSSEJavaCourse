package me.vladislav.homework01.app.core.exception.db.repository;

public class CourseNotFoundException extends RuntimeException {
  public CourseNotFoundException() {
    super("Requested course not found.");
  }
}


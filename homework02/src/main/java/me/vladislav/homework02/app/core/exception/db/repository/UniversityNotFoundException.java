package me.vladislav.homework02.app.core.exception.db.repository;

public class UniversityNotFoundException extends RuntimeException {
  public UniversityNotFoundException() {
    super("Requested university not found.");
  }
}
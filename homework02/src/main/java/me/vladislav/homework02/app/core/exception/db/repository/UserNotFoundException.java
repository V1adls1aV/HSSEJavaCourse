package me.vladislav.homework02.app.core.exception.db.repository;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super("Requested user not found.");
  }
}

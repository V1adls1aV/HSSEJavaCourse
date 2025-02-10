package me.vladislav.homework01.app.core.exception.db.repository;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super("Requested user not found.");
  }
}

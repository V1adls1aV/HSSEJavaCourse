package me.vladislav.homework02.app.core.exception.db.repository;

public class BookNotFoundException extends RuntimeException {
  public BookNotFoundException() {
    super("Requested book not found.");
  }
}

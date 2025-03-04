package me.vladislav.homework.app.dto.service;

public record Book(Long id, String title, String author) {
  public BookData getBookData() {
    return new BookData(title, author);
  }
}

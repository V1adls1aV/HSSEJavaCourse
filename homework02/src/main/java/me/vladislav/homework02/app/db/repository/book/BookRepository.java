package me.vladislav.homework02.app.db.repository.book;

import me.vladislav.homework02.app.dto.service.Book;
import me.vladislav.homework02.app.dto.service.BookData;

public interface BookRepository {
  Long create(BookData book);

  Book getById(Long id);

  Book update(Book book);

  void delete(Long id);
}

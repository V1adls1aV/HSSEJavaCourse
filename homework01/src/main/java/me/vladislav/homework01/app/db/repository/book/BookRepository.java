package me.vladislav.homework01.app.db.repository.book;

import me.vladislav.homework01.app.dto.service.Book;
import me.vladislav.homework01.app.dto.service.BookData;

public interface BookRepository {
  Long create(BookData book);

  Book getById(Long id);
}

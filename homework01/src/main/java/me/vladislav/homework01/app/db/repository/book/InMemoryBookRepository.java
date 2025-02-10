package me.vladislav.homework01.app.db.repository.book;

import me.vladislav.homework01.app.core.exception.db.repository.BookNotFoundException;
import me.vladislav.homework01.app.dto.service.Book;
import me.vladislav.homework01.app.dto.service.BookData;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryBookRepository implements BookRepository {
  private static final Map<Long, Book> storage = new ConcurrentHashMap<>();
  private static final AtomicLong idGenerator = new AtomicLong(1L);

  public InMemoryBookRepository() {

  }

  public Long create(BookData book) {
    Long id = idGenerator.getAndIncrement();
    storage.put(id, new Book(id, book.title(), book.author()));
    return id;
  }

  public Book getById(Long id) {
    Book book = storage.get(id);
    if (book == null) {
      throw new BookNotFoundException();
    }
    return book;
  }
}

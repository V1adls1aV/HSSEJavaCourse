package me.vladislav.homework01.app.db.repository.book;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework01.app.core.exception.db.repository.BookNotFoundException;
import me.vladislav.homework01.app.dto.service.Book;
import me.vladislav.homework01.app.dto.service.BookData;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class InMemoryBookRepository implements BookRepository {
  private static final Map<Long, Book> storage = new ConcurrentHashMap<>();
  private static final AtomicLong idGenerator = new AtomicLong(1L);

  public InMemoryBookRepository() {}

  public Long create(BookData book) {
    log.debug("Creating new book: {} by {}", book.title(), book.author());
    Long id = idGenerator.getAndIncrement();
    storage.put(id, new Book(id, book.title(), book.author()));
    log.debug("Created book with id: {}", id);
    return id;
  }

  public Book getById(Long id) {
    log.debug("Retrieving book with id: {}", id);
    Book book = storage.get(id);
    if (book == null) {
      log.error("Book not found with id: {}", id);
      throw new BookNotFoundException();
    }
    log.debug("Found book: {} by {}", book.title(), book.author());
    return book;
  }
}

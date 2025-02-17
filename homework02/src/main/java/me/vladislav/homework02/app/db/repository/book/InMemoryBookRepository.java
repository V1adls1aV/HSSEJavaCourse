package me.vladislav.homework02.app.db.repository.book;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework02.app.core.exception.db.repository.BookNotFoundException;
import me.vladislav.homework02.app.dto.service.Book;
import me.vladislav.homework02.app.dto.service.BookData;
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
      throw new BookNotFoundException();
    }
    log.debug("Found book: {} by {}", book.title(), book.author());
    return book;
  }

  @Override
  public Book update(Book book) {
    log.debug("Updating book with id: {}", book.id());
    if (!storage.containsKey(book.id())) {
      throw new BookNotFoundException();
    }
    storage.put(book.id(), book);
    log.debug("Updated book: {} by {}", book.title(), book.author());
    return book;
  }

  @Override
  public void delete(Long id) {
    log.debug("Deleting book with id: {}", id);
    if (!storage.containsKey(id)) {
      throw new BookNotFoundException();
    }
    storage.remove(id);
    log.debug("Deleted book with id: {}", id);
  }
}

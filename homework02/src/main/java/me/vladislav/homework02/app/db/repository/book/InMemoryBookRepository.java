package me.vladislav.homework02.app.db.repository.book;

import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework02.app.dto.service.Book;
import me.vladislav.homework02.app.dto.service.BookData;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class InMemoryBookRepository implements BookRepository {
  private static final Map<Long, Book> storage = new ConcurrentHashMap<>();
  private static final AtomicLong idGenerator = new AtomicLong(1L);

  public InMemoryBookRepository() {}

  public Book create(BookData book) {
    log.debug("Creating new book: {} by {}", book.title(), book.author());
    Long id = idGenerator.getAndIncrement();
    Book newBook = new Book(id, book.title(), book.author());
    storage.put(id, newBook);
    log.debug("Created book with id: {}", id);
    return newBook;
  }

  public Optional<Book> getById(Long id) {
    log.debug("Retrieving book with id: {}", id);
    Book book = storage.get(id);
    if (book != null) {
      log.debug("Found book: {} by {}", book.title(), book.author());
    } else {
      log.debug("No book found with id: {}", id);
    }
    return Optional.ofNullable(book);
  }

  @Override
  public Optional<Book> update(Book book) {
    log.debug("Updating book with id: {}", book.id());
    if (!storage.containsKey(book.id())) {
      return Optional.empty();
    }
    storage.put(book.id(), book);
    log.debug("Updated book: {} by {}", book.title(), book.author());
    return Optional.of(book);
  }

  @Override
  public void delete(Long id) {
    log.debug("Deleting book with id: {}", id);
    storage.remove(id);
    log.debug("Deleted book with id: {}", id);
  }
}

package me.vladislav.homework.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.api.route.broker.producer.AuditProducer;
import me.vladislav.homework.app.core.exception.db.repository.BookNotFoundException;
import me.vladislav.homework.app.db.orm.Book;
import me.vladislav.homework.app.db.orm.User;
import me.vladislav.homework.app.db.repository.BookRepository;
import me.vladislav.homework.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework.app.dto.api.request.BookPatchRequest;
import me.vladislav.homework.app.dto.api.request.BookUpdateRequest;
import me.vladislav.homework.app.dto.broker.OperationType;
import me.vladislav.homework.app.dto.broker.UserAuditData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
  private final BookRepository bookRepository;
  private final AuditProducer auditProducer;

  @Transactional
  public Book addNewBookForUser(Long userId, BookCreateRequest book) {
    log.info("Adding new book '{}' by {} for user {}", book.title(), book.author(), userId);

    Book newBook =
        bookRepository.save(new Book(null, book.title(), book.author(), new User(userId)));

    String message = String.format("Successfully added book for user %d", userId);
    log.info(message);
    auditProducer.sendMessage(
        new UserAuditData(userId, LocalDateTime.now(), OperationType.CREATE, message));

    return newBook;
  }

  @Cacheable("BookList")
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public List<Book> getBooksForUser(Long userId) {
    log.info("Retrieving books for user {}", userId);

    Set<Book> books = bookRepository.findByUserId(userId);

    String message = String.format("Found %d books for user %d", books.size(), userId);
    log.info(message);
    auditProducer.sendMessage(
        new UserAuditData(userId, LocalDateTime.now(), OperationType.READ, message));

    return books.stream().toList();
  }

  /*
   * Цель в данном случае – гарантировать обновление книги,
   * ведь при больших объемах данных (и при AP модели базы данных (из CAP теоремы))
   * запись может не успеть достичь той ноды базы данных, к которой мы обращаемся.
   */
  @Retryable(
      value = BookNotFoundException.class,
      maxAttempts = 5,
      backoff = @Backoff(delay = 10_000))
  @Transactional
  public Book updateBookForUser(Long userId, BookUpdateRequest bookRequest) {
    log.info("Updating book {} for user {}", bookRequest.id(), userId);

    Book updatedBook =
        bookRepository.save(
            new Book(
                bookRequest.id(),
                bookRequest.title(),
                bookRequest.author(),
                new User(userId, null, null)));

    String message =
        String.format("Successfully updated book %d for user %d", updatedBook.getId(), userId);
    log.info(message);
    auditProducer.sendMessage(
        new UserAuditData(userId, LocalDateTime.now(), OperationType.UPDATE, message));

    return updatedBook;
  }

  @Retryable(
      value = BookNotFoundException.class,
      maxAttempts = 5,
      backoff = @Backoff(delay = 10_000))
  @Transactional
  public Book partiallyUpdateBookForUser(Long userId, BookPatchRequest bookRequest) {
    log.info("Partially updating book {} for user {}", bookRequest.id(), userId);
    Book existingBook =
        bookRepository.findById(bookRequest.id()).orElseThrow(BookNotFoundException::new);

    if (bookRequest.title() != null) existingBook.setTitle(bookRequest.title());
    if (bookRequest.author() != null) existingBook.setAuthor(bookRequest.author());

    Book updatedBook = bookRepository.save(existingBook);

    String message =
        String.format(
            "Successfully partially updated book %d for user %d", updatedBook.getId(), userId);
    log.info(message);
    auditProducer.sendMessage(
        new UserAuditData(userId, LocalDateTime.now(), OperationType.UPDATE, message));

    return updatedBook;
  }

  @Transactional
  public Book deleteBookForUser(Long userId, Long bookId) {
    log.info("Deleting book {} for user {}", bookId, userId);

    Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
    bookRepository.delete(book);

    String message = String.format("Successfully deleted book %d for user %d", bookId, userId);
    log.info(message);
    auditProducer.sendMessage(
        new UserAuditData(userId, LocalDateTime.now(), OperationType.DELETE, message));

    return book;
  }
}

package me.vladislav.homework.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.core.exception.db.repository.BookNotFoundException;
import me.vladislav.homework.app.core.exception.db.repository.UserNotFoundException;
import me.vladislav.homework.app.db.orm.Book;
import me.vladislav.homework.app.db.orm.User;
import me.vladislav.homework.app.db.repository.BookRepository;
import me.vladislav.homework.app.db.repository.UserRepository;
import me.vladislav.homework.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework.app.dto.api.request.BookPatchRequest;
import me.vladislav.homework.app.dto.api.request.BookUpdateRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
  private final UserRepository userRepository;
  private final BookRepository bookRepository;

  @Transactional
  public Book addNewBookForUser(Long userId, BookCreateRequest book) {
    log.info("Adding new book '{}' by {} for user {}", book.title(), book.author(), userId);
    Book newBook = new Book(null, book.title(), book.author());

    User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    user.getBooks().add(newBook);

    log.info("Successfully added book with id {} for user {}", newBook.getId(), userId);
    return newBook;
  }

  @Cacheable("BookList")
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public List<Book> getBooksForUser(Long userId) {
    log.info("Retrieving books for user {}", userId);

    User user = userRepository.findUserWithBooks(userId).orElseThrow(UserNotFoundException::new);

    log.info("Found {} books for user {}", user.getBooks().size(), userId);
    return user.getBooks();
  }

  /*
   * Цель в данном случае – гарантировать обновление книги,
   * ведь при больших объемах данных (и при AP модели базы данных (из CAP теоремы))
   * запись может не успеть достичь той ноды базы данных, к которой мы обращаемся.
   */
  @Retryable(value = BookNotFoundException.class, maxAttempts = 5, backoff = @Backoff(delay = 10_000))
  @Transactional
  public Book updateBookForUser(Long userId, BookUpdateRequest bookRequest) {
    log.info("Updating book {} for user {}", bookRequest.id(), userId);
    Book updatedBook = bookRepository.save(new Book(bookRequest.id(), bookRequest.title(), bookRequest.author()));
    log.info("Successfully updated book {} for user {}", updatedBook.getId(), userId);
    return updatedBook;
  }

  @Retryable(value = BookNotFoundException.class, maxAttempts = 5, backoff = @Backoff(delay = 10_000))
  @Transactional
  public Book partiallyUpdateBookForUser(Long userId, BookPatchRequest bookRequest) {
    log.info("Partially updating book {} for user {}", bookRequest.id(), userId);
    Book existingBook =
        bookRepository.findById(bookRequest.id()).orElseThrow(BookNotFoundException::new);

    if (bookRequest.title() != null)
      existingBook.setTitle(bookRequest.title());
    if (bookRequest.author() != null)
      existingBook.setAuthor(bookRequest.author());

    Book updatedBook = bookRepository.save(existingBook);
    log.info("Successfully partially updated book {} for user {}", updatedBook.getId(), userId);
    return updatedBook;
  }

  @Transactional
  public Book deleteBookForUser(Long userId, Long bookId) {
    log.info("Deleting book {} for user {}", bookId, userId);

    Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
    bookRepository.delete(book);

    log.info("Successfully deleted book {} for user {}", bookId, userId);
    return book;
  }
}

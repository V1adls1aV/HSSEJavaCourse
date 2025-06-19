package me.vladislav.homework.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.core.exception.db.repository.BookNotFoundException;
import me.vladislav.homework.app.db.repository.book.BookRepository;
import me.vladislav.homework.app.db.repository.user.UserRepository;
import me.vladislav.homework.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework.app.dto.api.request.BookPatchRequest;
import me.vladislav.homework.app.dto.api.request.BookUpdateRequest;
import me.vladislav.homework.app.dto.service.Book;
import me.vladislav.homework.app.dto.service.BookData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
  private final UserRepository userRepository;
  private final BookRepository bookRepository;

  public Book addNewBookForUser(Long userId, BookCreateRequest book) {
    log.info("Adding new book '{}' by {} for user {}", book.title(), book.author(), userId);
    Book newBook = bookRepository.create(new BookData(book.title(), book.author()));
    userRepository.addBookId(userId, newBook.id());
    log.info("Successfully added book with id {} for user {}", newBook.id(), userId);
    return newBook;
  }

  public List<Book> getBooksForUser(Long userId) {
    log.info("Retrieving books for user {}", userId);
    Set<Long> bookIds = userRepository.getBooksIds(userId);
    List<Book> books =
        bookIds.parallelStream()
            .map(bookRepository::getById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    log.info("Found {} books for user {}", books.size(), userId);
    return books;
  }

  public Book updateBookForUser(Long userId, BookUpdateRequest bookRequest) {
    log.info("Updating book {} for user {}", bookRequest.id(), userId);
    Book book = new Book(bookRequest.id(), bookRequest.title(), bookRequest.author());
    Book updatedBook =
        bookRepository.update(book).orElse(bookRepository.create(book.getBookData()));
    log.info("Successfully updated book {} for user {}", book.id(), userId);
    return updatedBook;
  }

  public Book partiallyUpdateBookForUser(Long userId, BookPatchRequest bookRequest) {
    log.info("Partially updating book {} for user {}", bookRequest.id(), userId);

    Set<Long> userBookIds = userRepository.getBooksIds(userId);
    if (!userBookIds.contains(bookRequest.id())) {
      throw new BookNotFoundException();
    }

    Book existingBook =
        bookRepository.getById(bookRequest.id()).orElseThrow(BookNotFoundException::new);
    String newTitle = bookRequest.title() != null ? bookRequest.title() : existingBook.title();
    String newAuthor = bookRequest.author() != null ? bookRequest.author() : existingBook.author();

    Book book = new Book(bookRequest.id(), newTitle, newAuthor);
    // If book does not exist yet, exception will be raised a few lines before
    Book updatedBook = bookRepository.update(book).orElseThrow(BookNotFoundException::new);
    log.info("Successfully partially updated book {} for user {}", book.id(), userId);
    return updatedBook;
  }

  public Book deleteBookForUser(Long userId, Long bookId) {
    log.info("Deleting book {} for user {}", bookId, userId);

    Set<Long> userBookIds = userRepository.getBooksIds(userId);
    if (!userBookIds.contains(bookId)) {
      throw new BookNotFoundException();
    }

    Book book = bookRepository.getById(bookId).orElseThrow(BookNotFoundException::new);
    bookRepository.delete(bookId);
    userBookIds.remove(bookId);
    log.info("Successfully deleted book {} for user {}", bookId, userId);
    return book;
  }
}

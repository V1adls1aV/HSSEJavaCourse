package me.vladislav.homework01.app.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework01.app.db.repository.book.BookRepository;
import me.vladislav.homework01.app.db.repository.user.UserRepository;
import me.vladislav.homework01.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework01.app.dto.service.Book;
import me.vladislav.homework01.app.dto.service.BookData;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookService {
  private final UserRepository userRepository;
  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository, UserRepository userRepository) {
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
  }

  public void addNewBookForUser(Long userId, BookCreateRequest book) {
    log.info("Adding new book '{}' by {} for user {}", book.title(), book.author(), userId);
    Long bookId = bookRepository.create(new BookData(book.title(), book.author()));
    userRepository.addBookId(userId, bookId);
    log.info("Successfully added book with id {} for user {}", bookId, userId);
  }

  public List<Book> getBooksForUser(Long userId) {
    log.info("Retrieving books for user {}", userId);
    Set<Long> bookIds = userRepository.getBooksIds(userId);
    List<Book> books =
        bookIds.parallelStream().map(bookRepository::getById).collect(Collectors.toList());
    log.info("Found {} books for user {}", books.size(), userId);
    return books;
  }
}

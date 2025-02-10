package me.vladislav.homework01.app.service;

import me.vladislav.homework01.app.db.repository.book.BookRepository;
import me.vladislav.homework01.app.db.repository.user.UserRepository;
import me.vladislav.homework01.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework01.app.dto.service.Book;
import me.vladislav.homework01.app.dto.service.BookData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
  private final UserRepository userRepository;
  private final BookRepository bookRepository;

  @Autowired
  public BookService(BookRepository bookRepository, UserRepository userRepository) {
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
  }

  public void addNewBookForUser(Long userId, BookCreateRequest book) {
    Long bookId = bookRepository.create(new BookData(book.title(), book.author()));
    userRepository.addBookId(userId, bookId);
  }

  public List<Book> getBooksForUser(Long userId) {
    Set<Long> bookIds = userRepository.getBooksIds(userId);
    return bookIds.parallelStream().map(bookRepository::getById).collect(Collectors.toList());
  }
}

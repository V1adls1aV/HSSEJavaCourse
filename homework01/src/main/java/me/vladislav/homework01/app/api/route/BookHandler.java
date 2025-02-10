package me.vladislav.homework01.app.api.route;

import me.vladislav.homework01.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework01.app.dto.api.response.BookGetResponse;
import me.vladislav.homework01.app.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/book")
public class BookHandler {
  private final BookService bookService;

  @Autowired
  public BookHandler(BookService bookService) {
    this.bookService = bookService;
  }

  @PutMapping("/user/{userId}")
  public void addBookForUser(@PathVariable Long userId, @RequestBody BookCreateRequest book) {
    bookService.addNewBookForUser(userId, book);
  }

  @GetMapping("/user/{userId}")
  public List<BookGetResponse> getBooksForUser(@PathVariable Long userId) {
    return bookService.getBooksForUser(userId).stream()
        .map(book -> new BookGetResponse(book.id(), book.title(), book.author()))
        .collect(Collectors.toList());
  }
}

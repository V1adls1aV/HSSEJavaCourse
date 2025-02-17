package me.vladislav.homework02.app.api.route;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import me.vladislav.homework02.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework02.app.dto.api.response.BookGetResponse;
import me.vladislav.homework02.app.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
public class BookHandler {
  private final BookService bookService;

  public BookHandler(BookService bookService) {
    this.bookService = bookService;
  }

  @PutMapping("/user/{userId}")
  public ResponseEntity<Void> addBookForUser(
      @PathVariable Long userId, @Valid @RequestBody BookCreateRequest book) {
    bookService.addNewBookForUser(userId, book);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<BookGetResponse>> getBooksForUser(@PathVariable Long userId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            bookService.getBooksForUser(userId).stream()
                .map(book -> new BookGetResponse(book.id(), book.title(), book.author()))
                .collect(Collectors.toList()));
  }
}

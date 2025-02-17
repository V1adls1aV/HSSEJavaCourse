package me.vladislav.homework02.app.api.route;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import me.vladislav.homework02.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework02.app.dto.api.request.BookPatchRequest;
import me.vladislav.homework02.app.dto.api.request.BookUpdateRequest;
import me.vladislav.homework02.app.dto.api.response.BookGetResponse;
import me.vladislav.homework02.app.dto.service.Book;
import me.vladislav.homework02.app.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/{userId}/book")
public class BookHandler {
  private final BookService bookService;

  public BookHandler(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping
  public ResponseEntity<List<BookGetResponse>> getBooksForUser(@PathVariable Long userId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            bookService.getBooksForUser(userId).stream()
                .map(book -> new BookGetResponse(book.id(), book.title(), book.author()))
                .collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<BookGetResponse> addBookForUser(
      @PathVariable Long userId, @Valid @RequestBody BookCreateRequest book) {
    Book createdBook = bookService.addNewBookForUser(userId, book);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BookGetResponse(createdBook.id(), createdBook.title(), createdBook.author()));
  }

  @PutMapping
  public ResponseEntity<BookGetResponse> updateBookForUser(
      @PathVariable Long userId, @Valid @RequestBody BookUpdateRequest book) {
    Book updatedBook = bookService.updateBookForUser(userId, book);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BookGetResponse(updatedBook.id(), updatedBook.title(), updatedBook.author()));
  }

  @PatchMapping
  public ResponseEntity<BookGetResponse> partiallyUpdateBookForUser(
      @PathVariable Long userId, @Valid @RequestBody BookPatchRequest book) {
    Book updatedBook = bookService.partiallyUpdateBookForUser(userId, book);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BookGetResponse(updatedBook.id(), updatedBook.title(), updatedBook.author()));
  }

  @DeleteMapping("/{bookId}")
  public ResponseEntity<BookGetResponse> deleteBookForUser(
      @PathVariable Long userId, @PathVariable Long bookId) {
    Book deletedBook = bookService.deleteBookForUser(userId, bookId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BookGetResponse(deletedBook.id(), deletedBook.title(), deletedBook.author()));
  }
}

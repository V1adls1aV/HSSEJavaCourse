package me.vladislav.homework.app.api.route;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import me.vladislav.homework.app.api.route.annotation.BookControllerAnnotation;
import me.vladislav.homework.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework.app.dto.api.request.BookPatchRequest;
import me.vladislav.homework.app.dto.api.request.BookUpdateRequest;
import me.vladislav.homework.app.dto.api.response.BookGetResponse;
import me.vladislav.homework.app.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/{userId}/book")
public class BookController implements BookControllerAnnotation {
  private final BookService bookService;
  private final CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("BookControllerCircuitBreaker");

  @GetMapping
  public ResponseEntity<List<BookGetResponse>> getBooksForUser(Long userId) {
    return circuitBreaker.executeSupplier(() -> {
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              bookService.getBooksForUser(userId).stream()
                  .map(book -> new BookGetResponse(book.getId(), book.getTitle(), book.getAuthor()))
                  .collect(Collectors.toList()));
    });
  }

  @PostMapping
  public ResponseEntity<BookGetResponse> addBookForUser(Long userId, BookCreateRequest book) {
    return circuitBreaker.executeSupplier(() -> {
      var createdBook = bookService.addNewBookForUser(userId, book);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(new BookGetResponse(createdBook.getId(), createdBook.getTitle(), createdBook.getAuthor()));
    });
  }

  @PutMapping
  public ResponseEntity<BookGetResponse> updateBookForUser(Long userId, BookUpdateRequest book) {
    return circuitBreaker.executeSupplier(() -> {
      var updatedBook = bookService.updateBookForUser(userId, book);
      return ResponseEntity.status(HttpStatus.OK)
          .body(new BookGetResponse(updatedBook.getId(), updatedBook.getTitle(), updatedBook.getAuthor()));
    });
  }

  @PatchMapping
  public ResponseEntity<BookGetResponse> partiallyUpdateBookForUser(
      Long userId, BookPatchRequest book) {
    return circuitBreaker.executeSupplier(() -> {
      var updatedBook = bookService.partiallyUpdateBookForUser(userId, book);
      return ResponseEntity.status(HttpStatus.OK)
          .body(new BookGetResponse(updatedBook.getId(), updatedBook.getTitle(), updatedBook.getAuthor()));
    });
  }

  @DeleteMapping("/{bookId}")
  public ResponseEntity<BookGetResponse> deleteBookForUser(Long userId, Long bookId) {
    return circuitBreaker.executeSupplier(() -> {
      var deletedBook = bookService.deleteBookForUser(userId, bookId);
      return ResponseEntity.status(HttpStatus.OK)
          .body(new BookGetResponse(deletedBook.getId(), deletedBook.getTitle(), deletedBook.getAuthor()));
    });
  }
}

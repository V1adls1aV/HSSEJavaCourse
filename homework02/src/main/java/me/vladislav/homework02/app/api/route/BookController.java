package me.vladislav.homework02.app.api.route;

import lombok.RequiredArgsConstructor;
import me.vladislav.homework02.app.api.route.annotation.BookControllerAnnotation;
import me.vladislav.homework02.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework02.app.dto.api.request.BookPatchRequest;
import me.vladislav.homework02.app.dto.api.request.BookUpdateRequest;
import me.vladislav.homework02.app.dto.api.response.BookGetResponse;
import me.vladislav.homework02.app.service.BookService;
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

  @GetMapping
  public ResponseEntity<List<BookGetResponse>> getBooksForUser(Long userId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            bookService.getBooksForUser(userId).stream()
                .map(book -> new BookGetResponse(book.id(), book.title(), book.author()))
                .collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<BookGetResponse> addBookForUser(Long userId, BookCreateRequest book) {
    var createdBook = bookService.addNewBookForUser(userId, book);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BookGetResponse(createdBook.id(), createdBook.title(), createdBook.author()));
  }

  @PutMapping
  public ResponseEntity<BookGetResponse> updateBookForUser(Long userId, BookUpdateRequest book) {
    var updatedBook = bookService.updateBookForUser(userId, book);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BookGetResponse(updatedBook.id(), updatedBook.title(), updatedBook.author()));
  }

  @PatchMapping
  public ResponseEntity<BookGetResponse> partiallyUpdateBookForUser(
      Long userId, BookPatchRequest book) {
    var updatedBook = bookService.partiallyUpdateBookForUser(userId, book);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BookGetResponse(updatedBook.id(), updatedBook.title(), updatedBook.author()));
  }

  @DeleteMapping("/{bookId}")
  public ResponseEntity<BookGetResponse> deleteBookForUser(Long userId, Long bookId) {
    var deletedBook = bookService.deleteBookForUser(userId, bookId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BookGetResponse(deletedBook.id(), deletedBook.title(), deletedBook.author()));
  }
}

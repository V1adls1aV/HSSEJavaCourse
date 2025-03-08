package me.vladislav.homework.app;

import jakarta.annotation.PostConstruct;
import me.vladislav.homework.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework.app.dto.api.request.BookPatchRequest;
import me.vladislav.homework.app.dto.api.request.BookUpdateRequest;
import me.vladislav.homework.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework.app.dto.api.response.BookGetResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests full book's lifespan separately.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookE2ETest {
  @Autowired
  private TestRestTemplate restTemplate;

  @PostConstruct
  public void setUp() {
    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }

  @Test
  public void testBookLifecycle() {
    // Create a user first
    UserCreateRequest userRequest = new UserCreateRequest("bookuser", "books@test.com");
    ResponseEntity<Long> createUserResponse =
        restTemplate.postForEntity("/api/user", userRequest, Long.class);
    assertTrue(createUserResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
    Long userId = createUserResponse.getBody();
    assertNotNull(userId);

    // Create a book
    BookCreateRequest createRequest = new BookCreateRequest("1984", "George");
    ResponseEntity<BookGetResponse> createBookResponse =
        restTemplate.postForEntity(
            "/api/user/" + userId + "/book", createRequest, BookGetResponse.class);
    assertTrue(createBookResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
    BookGetResponse createdBook = createBookResponse.getBody();
    assertNotNull(createdBook);
    assertEquals(createRequest.title(), createdBook.title());
    assertEquals(createRequest.author(), createdBook.author());
    assertNotNull(createdBook.id());

    // Update the book
    BookUpdateRequest updateRequest =
        new BookUpdateRequest(createdBook.id(), "1984 - Updated", "George Orwell");
    ResponseEntity<BookGetResponse> updateBookResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/book",
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest),
            BookGetResponse.class);
    assertTrue(updateBookResponse.getStatusCode().is2xxSuccessful());
    BookGetResponse updatedBook = updateBookResponse.getBody();
    assertNotNull(updatedBook);
    assertEquals(updateRequest.title(), updatedBook.title());
    assertEquals(updateRequest.author(), updatedBook.author());
    assertEquals(createdBook.id(), updatedBook.id());

    // Partially update the book
    BookPatchRequest patchRequest =
        new BookPatchRequest(
            createdBook.id(), "1984 - Patched", null // Keep the original author
        );
    ResponseEntity<BookGetResponse> patchBookResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/book",
            HttpMethod.PATCH,
            new HttpEntity<>(patchRequest),
            BookGetResponse.class);
    assertTrue(patchBookResponse.getStatusCode().is2xxSuccessful());
    BookGetResponse patchedBook = patchBookResponse.getBody();
    assertNotNull(patchedBook);
    assertEquals(patchRequest.title(), patchedBook.title());
    // Check the author changed in previous request
    assertEquals(updateRequest.author(), patchedBook.author());
    assertEquals(createdBook.id(), patchedBook.id());

    // Delete the book
    ResponseEntity<BookGetResponse> deleteBookResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/book/" + createdBook.id(),
            HttpMethod.DELETE,
            null,
            BookGetResponse.class);
    assertTrue(deleteBookResponse.getStatusCode().is2xxSuccessful());

    // Verify the book is deleted by trying to get all books
    ResponseEntity<List<BookGetResponse>> getBooksResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/book",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            });
    assertTrue(getBooksResponse.getStatusCode().is2xxSuccessful());
    List<BookGetResponse> books = getBooksResponse.getBody();
    assertNotNull(books);
    assertTrue(books.isEmpty());
  }
}

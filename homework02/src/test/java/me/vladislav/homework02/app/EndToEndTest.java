package me.vladislav.homework02.app;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.annotation.PostConstruct;
import java.util.List;
import me.vladislav.homework02.app.dto.api.request.*;
import me.vladislav.homework02.app.dto.api.response.BookResponse;
import me.vladislav.homework02.app.dto.api.response.CourseGetResponse;
import me.vladislav.homework02.app.dto.api.response.UniversityGetResponse;
import me.vladislav.homework02.app.dto.api.response.UserGetResponse;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTest {

  @Autowired private TestRestTemplate restTemplate;

  @PostConstruct
  public void setUp() {
    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }

  @Test
  public void testFullUserFlow() {
    // Create a user
    UserCreateRequest userRequest = new UserCreateRequest("vladislav", "me@vladislav.ru");
    ResponseEntity<Long> createUserResponse =
        restTemplate.postForEntity("/api/user", userRequest, Long.class);
    assertTrue(createUserResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
    Long userId = createUserResponse.getBody();
    assertNotNull(userId);

    // Add books
    BookCreateRequest book1 = new BookCreateRequest("The Lord of the Rings", "J.R.R. Tolkien");
    BookCreateRequest book2 = new BookCreateRequest("1984", "George Orwell");

    assertTrue(
        restTemplate
            .postForEntity("/api/user/" + userId + "/book", book1, BookResponse.class)
            .getStatusCode()
            .is2xxSuccessful());
    assertTrue(
        restTemplate
            .postForEntity("/api/user/" + userId + "/book", book2, BookResponse.class)
            .getStatusCode()
            .is2xxSuccessful());

    // Add courses
    CourseCreateRequest course1 =
        new CourseCreateRequest("Java Programming", "MTS", "Basic Java", 40);
    CourseCreateRequest course2 =
        new CourseCreateRequest("Spring Framework", "MTS", "Spring basics", 30);

    assertTrue(
        restTemplate
            .postForEntity("/api/user/" + userId + "/course", course1, CourseGetResponse.class)
            .getStatusCode()
            .is2xxSuccessful());
    assertTrue(
        restTemplate
            .postForEntity("/api/user/" + userId + "/course", course2, CourseGetResponse.class)
            .getStatusCode()
            .is2xxSuccessful());

    // Add universities
    UniversityCreateRequest uni1 =
        new UniversityCreateRequest("MIT", "Cambridge", "Technical University", 100);
    UniversityCreateRequest uni2 = new UniversityCreateRequest("MIPT", "Dolgopa", "The hell?", 999);

    assertTrue(
        restTemplate
            .postForEntity("/api/user/" + userId + "/university", uni1, UniversityGetResponse.class)
            .getStatusCode()
            .is2xxSuccessful());
    assertTrue(
        restTemplate
            .postForEntity("/api/user/" + userId + "/university", uni2, UniversityGetResponse.class)
            .getStatusCode()
            .is2xxSuccessful());

    // Get user data
    ResponseEntity<UserGetResponse> userResponse =
        restTemplate.getForEntity("/api/user/" + userId, UserGetResponse.class);
    assertTrue(userResponse.getStatusCode().is2xxSuccessful());
    assertNotNull(userResponse.getBody());
    assertEquals("vladislav", userResponse.getBody().username());

    // Get books
    ResponseEntity<List<BookResponse>> booksResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/book",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
    assertTrue(booksResponse.getStatusCode().is2xxSuccessful());
    List<BookResponse> books = booksResponse.getBody();
    assertNotNull(books);
    assertEquals(2, books.size());
    assertTrue(books.stream().anyMatch(b -> b.title().equals("The Lord of the Rings")));
    assertTrue(books.stream().anyMatch(b -> b.title().equals("1984")));

    // Get courses
    ResponseEntity<List<CourseGetResponse>> coursesResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/course",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
    assertTrue(coursesResponse.getStatusCode().is2xxSuccessful());
    List<CourseGetResponse> courses = coursesResponse.getBody();
    assertNotNull(courses);
    assertEquals(2, courses.size());
    assertTrue(courses.stream().anyMatch(c -> c.title().equals("Java Programming")));
    assertTrue(courses.stream().anyMatch(c -> c.title().equals("Spring Framework")));

    // Get universities
    ResponseEntity<List<UniversityGetResponse>> universitiesResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/university",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
    assertTrue(universitiesResponse.getStatusCode().is2xxSuccessful());
    List<UniversityGetResponse> universities = universitiesResponse.getBody();
    assertNotNull(universities);
    assertEquals(2, universities.size());
    assertTrue(universities.stream().anyMatch(u -> u.name().equals("MIT")));
    assertTrue(universities.stream().anyMatch(u -> u.name().equals("MIPT")));
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
    ResponseEntity<BookResponse> createBookResponse =
        restTemplate.postForEntity(
            "/api/user/" + userId + "/book", createRequest, BookResponse.class);
    assertTrue(createBookResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
    BookResponse createdBook = createBookResponse.getBody();
    assertNotNull(createdBook);
    assertEquals(createRequest.title(), createdBook.title());
    assertEquals(createRequest.author(), createdBook.author());
    assertNotNull(createdBook.id());

    // Update the book
    BookUpdateRequest updateRequest =
        new BookUpdateRequest(createdBook.id(), "1984 - Updated", "George Orwell");
    ResponseEntity<BookResponse> updateBookResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/book",
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest),
            BookResponse.class);
    assertTrue(updateBookResponse.getStatusCode().is2xxSuccessful());
    BookResponse updatedBook = updateBookResponse.getBody();
    assertNotNull(updatedBook);
    assertEquals(updateRequest.title(), updatedBook.title());
    assertEquals(updateRequest.author(), updatedBook.author());
    assertEquals(createdBook.id(), updatedBook.id());

    // Partially update the book
    BookPatchRequest patchRequest =
        new BookPatchRequest(
            createdBook.id(), "1984 - Patched", null // Keep the original author
            );
    ResponseEntity<BookResponse> patchBookResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/book",
            HttpMethod.PATCH,
            new HttpEntity<>(patchRequest),
            BookResponse.class);
    assertTrue(patchBookResponse.getStatusCode().is2xxSuccessful());
    BookResponse patchedBook = patchBookResponse.getBody();
    assertNotNull(patchedBook);
    assertEquals(patchRequest.title(), patchedBook.title());
    // Check the author changed in previous request
    assertEquals(updateRequest.author(), patchedBook.author());
    assertEquals(createdBook.id(), patchedBook.id());

    // Delete the book
    ResponseEntity<BookResponse> deleteBookResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/book/" + createdBook.id(),
            HttpMethod.DELETE,
            null,
            BookResponse.class);
    assertTrue(deleteBookResponse.getStatusCode().is2xxSuccessful());

    // Verify the book is deleted by trying to get all books
    ResponseEntity<List<BookResponse>> getBooksResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/book",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
    assertTrue(getBooksResponse.getStatusCode().is2xxSuccessful());
    List<BookResponse> books = getBooksResponse.getBody();
    assertNotNull(books);
    assertTrue(books.isEmpty());
  }
}

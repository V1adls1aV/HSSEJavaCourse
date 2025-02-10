package me.vladislav.homework01.app;

import me.vladislav.homework01.app.dto.api.request.BookCreateRequest;
import me.vladislav.homework01.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework01.app.dto.api.request.UniversityCreateRequest;
import me.vladislav.homework01.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework01.app.dto.api.response.BookGetResponse;
import me.vladislav.homework01.app.dto.api.response.CourseGetResponse;
import me.vladislav.homework01.app.dto.api.response.UniversityGetResponse;
import me.vladislav.homework01.app.dto.api.response.UserGetResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testFullUserFlow() {
        // Create a user
        UserCreateRequest userRequest = new UserCreateRequest("vladislav", "verystrongpassword228");
        ResponseEntity<Long> createUserResponse = restTemplate.postForEntity("/api/user/", userRequest, Long.class);
        assertTrue(createUserResponse.getStatusCode().is2xxSuccessful());
        Long userId = createUserResponse.getBody();
        assertNotNull(userId);

        // Add books
        BookCreateRequest book1 = new BookCreateRequest("The Lord of the Rings", "J.R.R. Tolkien");
        BookCreateRequest book2 = new BookCreateRequest("1984", "George Orwell");

        restTemplate.put("/api/book/user/" + userId, book1);
        restTemplate.put("/api/book/user/" + userId, book2);

        // Add courses
        CourseCreateRequest course1 = new CourseCreateRequest("Java Programming", "MTS", "Basic Java", 40);
        CourseCreateRequest course2 = new CourseCreateRequest("Spring Framework", "MTS", "Spring basics", 30);

        restTemplate.put("/api/course/user/" + userId, course1);
        restTemplate.put("/api/course/user/" + userId, course2);

        // Add universities
        UniversityCreateRequest uni1 = new UniversityCreateRequest("MIT", "Cambridge", "Technical University", 100);
        UniversityCreateRequest uni2 = new UniversityCreateRequest("MIPT", "Dolgopa", "The hell?", 999);

        restTemplate.put("/api/university/user/" + userId, uni1);
        restTemplate.put("/api/university/user/" + userId, uni2);

        // Get user data
        ResponseEntity<UserGetResponse> userResponse = restTemplate.getForEntity("/api/user/" + userId, UserGetResponse.class);
        assertTrue(userResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(userResponse.getBody());
        assertEquals("testUser", userResponse.getBody().username());

        // Get books
        ResponseEntity<List<BookGetResponse>> booksResponse = restTemplate.exchange(
            "/api/book/user/" + userId,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );
        assertTrue(booksResponse.getStatusCode().is2xxSuccessful());
        List<BookGetResponse> books = booksResponse.getBody();
        assertNotNull(books);
        assertEquals(2, books.size());
        assertTrue(books.stream().anyMatch(b -> b.title().equals("The Lord of the Rings")));
        assertTrue(books.stream().anyMatch(b -> b.title().equals("1984")));

        // Get courses
        ResponseEntity<List<CourseGetResponse>> coursesResponse = restTemplate.exchange(
            "/api/course/user/" + userId,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );
        assertTrue(coursesResponse.getStatusCode().is2xxSuccessful());
        List<CourseGetResponse> courses = coursesResponse.getBody();
        assertNotNull(courses);
        assertEquals(2, courses.size());
        assertTrue(courses.stream().anyMatch(c -> c.title().equals("Java Programming")));
        assertTrue(courses.stream().anyMatch(c -> c.title().equals("Spring Framework")));

        // Get universities
        ResponseEntity<List<UniversityGetResponse>> universitiesResponse = restTemplate.exchange(
            "/api/university/user/" + userId,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );
        assertTrue(universitiesResponse.getStatusCode().is2xxSuccessful());
        List<UniversityGetResponse> universities = universitiesResponse.getBody();
        assertNotNull(universities);
        assertEquals(2, universities.size());
        assertTrue(universities.stream().anyMatch(u -> u.name().equals("MIT")));
        assertTrue(universities.stream().anyMatch(u -> u.name().equals("MIPT")));
    }
}
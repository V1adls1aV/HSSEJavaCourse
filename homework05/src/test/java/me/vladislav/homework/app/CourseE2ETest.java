package me.vladislav.homework.app;

import jakarta.annotation.PostConstruct;
import me.vladislav.homework.app.config.TestMockitoConfig;
import me.vladislav.homework.app.db.repository.user.UserRepository;
import me.vladislav.homework.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework.app.dto.api.request.CoursePatchRequest;
import me.vladislav.homework.app.dto.api.request.CourseUpdateRequest;
import me.vladislav.homework.app.dto.api.request.UserCreateRequest;
import me.vladislav.homework.app.dto.api.response.CourseGetResponse;
import me.vladislav.homework.app.dto.service.UserData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * Tests full course's lifespan separately.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestMockitoConfig.class)
public class CourseE2ETest {
  @Autowired
  private TestRestTemplate restTemplate;

  @SpyBean
  private UserRepository userRepository;

  @PostConstruct
  public void setUp() {
    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }

  @Test
  public void testCourseLifecycle() {
    // Create a user first
    UserCreateRequest userRequest = new UserCreateRequest("courseuser", "courses@test.com");
    ResponseEntity<Long> createUserResponse =
        restTemplate.postForEntity("/api/user", userRequest, Long.class);
    assertTrue(createUserResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
    Long userId = createUserResponse.getBody();
    assertNotNull(userId);
    assertEquals(1L, userId);

    // Verify that spy was called
    verify(userRepository).create(any(UserData.class));

    // Create a course
    CourseCreateRequest createRequest =
        new CourseCreateRequest("Spring Boot", "MTS", "Learn Spring Boot", 40, UUID.randomUUID());
    ResponseEntity<CourseGetResponse> createCourseResponse =
        restTemplate.postForEntity(
            "/api/user/" + userId + "/course", createRequest, CourseGetResponse.class);
    assertTrue(createCourseResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
    CourseGetResponse createdCourse = createCourseResponse.getBody();
    assertNotNull(createdCourse);
    assertEquals(createRequest.title(), createdCourse.title());
    assertEquals(createRequest.author(), createdCourse.author());
    assertNotNull(createdCourse.id());

    // Update the course
    CourseUpdateRequest updateRequest =
        new CourseUpdateRequest(
            createdCourse.id(), "Spring Boot Advanced", "MTSSS", "Advanced Spring Boot", 80);
    ResponseEntity<CourseGetResponse> updateCourseResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/course",
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest),
            CourseGetResponse.class);
    assertTrue(updateCourseResponse.getStatusCode().is2xxSuccessful());
    CourseGetResponse updatedCourse = updateCourseResponse.getBody();
    assertNotNull(updatedCourse);
    assertEquals(updateRequest.title(), updatedCourse.title());
    assertEquals(updateRequest.author(), updatedCourse.author());
    assertEquals(createdCourse.id(), updatedCourse.id());

    // Partially update the course
    CoursePatchRequest patchRequest =
        new CoursePatchRequest(
            createdCourse.id(),
            "Spring Boot Mastery",
            null, // Keep the original author
            null, // Keep the original description
            100);
    ResponseEntity<CourseGetResponse> patchCourseResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/course",
            HttpMethod.PATCH,
            new HttpEntity<>(patchRequest),
            CourseGetResponse.class);
    assertTrue(patchCourseResponse.getStatusCode().is2xxSuccessful());
    CourseGetResponse patchedCourse = patchCourseResponse.getBody();
    assertNotNull(patchedCourse);
    assertEquals(patchRequest.title(), patchedCourse.title());
    assertEquals(updateRequest.author(), patchedCourse.author());
    assertEquals(createdCourse.id(), patchedCourse.id());

    // Delete the course
    ResponseEntity<CourseGetResponse> deleteCourseResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/course/" + createdCourse.id(),
            HttpMethod.DELETE,
            null,
            CourseGetResponse.class);
    assertTrue(deleteCourseResponse.getStatusCode().is2xxSuccessful());

    // Verify the course is deleted by trying to get all courses
    ResponseEntity<List<CourseGetResponse>> getCoursesResponse =
        restTemplate.exchange(
            "/api/user/" + userId + "/course",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            });
    assertTrue(getCoursesResponse.getStatusCode().is2xxSuccessful());
    List<CourseGetResponse> courses = getCoursesResponse.getBody();
    assertNotNull(courses);
    assertTrue(courses.isEmpty());
  }
}

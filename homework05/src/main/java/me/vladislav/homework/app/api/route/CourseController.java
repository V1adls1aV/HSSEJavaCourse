package me.vladislav.homework.app.api.route;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import me.vladislav.homework.app.api.route.annotation.CourseControllerAnnotation;
import me.vladislav.homework.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework.app.dto.api.request.CoursePatchRequest;
import me.vladislav.homework.app.dto.api.request.CourseUpdateRequest;
import me.vladislav.homework.app.dto.api.response.CourseGetResponse;
import me.vladislav.homework.app.db.orm.Course;
import me.vladislav.homework.app.service.CourseService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/{userId}/course")
public class CourseController implements CourseControllerAnnotation {
  private final CourseService courseService;
  private final CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("CourseControllerCircuitBreaker");
  private final RateLimiter rateLimiter = RateLimiter.ofDefaults("CourseControllerRateLimiter");

  @PostMapping
  public ResponseEntity<CourseGetResponse> addCourseForUser(
      Long userId, CourseCreateRequest course) {
    return circuitBreaker.executeSupplier(() -> rateLimiter.executeSupplier(() -> {
      Optional<Course> createdCourse = courseService.addNewCourseForUser(userId, course);
      if (createdCourse.isPresent()) {
        var presentCourse = createdCourse.get();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                new CourseGetResponse(
                    presentCourse.id(),
                    presentCourse.title(),
                    presentCourse.author(),
                    presentCourse.description(),
                    presentCourse.duration()));
      } else {
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
      }
    }));
  }

  @GetMapping
  public ResponseEntity<List<CourseGetResponse>> getCoursesForUser(Long userId) {
    return circuitBreaker.executeSupplier(() -> rateLimiter.executeSupplier(() -> {
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              courseService.getCoursesForUser(userId).stream()
                  .map(
                      course ->
                          new CourseGetResponse(
                              course.id(),
                              course.title(),
                              course.author(),
                              course.description(),
                              course.duration()))
                  .collect(Collectors.toList()));
    }));
  }

  @PutMapping
  public ResponseEntity<CourseGetResponse> updateCourseForUser(
      Long userId, CourseUpdateRequest course) {
    return circuitBreaker.executeSupplier(() -> rateLimiter.executeSupplier(() -> {
      var updatedCourse = courseService.updateCourseForUser(userId, course);
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new CourseGetResponse(
                  updatedCourse.id(),
                  updatedCourse.title(),
                  updatedCourse.author(),
                  updatedCourse.description(),
                  updatedCourse.duration()));
    }));
  }

  @PatchMapping
  public ResponseEntity<CourseGetResponse> partiallyUpdateCourseForUser(
      Long userId, CoursePatchRequest course) {
    return circuitBreaker.executeSupplier(() -> rateLimiter.executeSupplier(() -> {
      var updatedCourse = courseService.partiallyUpdateCourseForUser(userId, course);
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new CourseGetResponse(
                  updatedCourse.id(),
                  updatedCourse.title(),
                  updatedCourse.author(),
                  updatedCourse.description(),
                  updatedCourse.duration()));
    }));
  }

  @DeleteMapping("/{courseId}")
  public ResponseEntity<CourseGetResponse> deleteCourseForUser(
      Long userId, Long courseId) {
    return circuitBreaker.executeSupplier(() -> rateLimiter.executeSupplier(() -> {
      var deletedCourse = courseService.deleteCourseForUser(userId, courseId);
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new CourseGetResponse(
                  deletedCourse.id(),
                  deletedCourse.title(),
                  deletedCourse.author(),
                  deletedCourse.description(),
                  deletedCourse.duration()));
    }));
  }
}

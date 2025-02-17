package me.vladislav.homework02.app.api.route;

import lombok.RequiredArgsConstructor;
import me.vladislav.homework02.app.api.route.annotation.CourseControllerAnnotation;
import me.vladislav.homework02.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework02.app.dto.api.request.CoursePatchRequest;
import me.vladislav.homework02.app.dto.api.request.CourseUpdateRequest;
import me.vladislav.homework02.app.dto.api.response.CourseGetResponse;
import me.vladislav.homework02.app.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/{userId}/course")
public class CourseController implements CourseControllerAnnotation {
  private final CourseService courseService;

  @PostMapping
  public ResponseEntity<CourseGetResponse> addCourseForUser(
      Long userId, CourseCreateRequest course) {
    var createdCourse = courseService.addNewCourseForUser(userId, course);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            new CourseGetResponse(
                createdCourse.id(),
                createdCourse.title(),
                createdCourse.author(),
                createdCourse.description(),
                createdCourse.duration()));
  }

  @GetMapping
  public ResponseEntity<List<CourseGetResponse>> getCoursesForUser(Long userId) {
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
  }

  @PutMapping
  public ResponseEntity<CourseGetResponse> updateCourseForUser(
      Long userId, CourseUpdateRequest course) {
    var updatedCourse = courseService.updateCourseForUser(userId, course);
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            new CourseGetResponse(
                updatedCourse.id(),
                updatedCourse.title(),
                updatedCourse.author(),
                updatedCourse.description(),
                updatedCourse.duration()));
  }

  @PatchMapping
  public ResponseEntity<CourseGetResponse> partiallyUpdateCourseForUser(
      Long userId, CoursePatchRequest course) {
    var updatedCourse = courseService.partiallyUpdateCourseForUser(userId, course);
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            new CourseGetResponse(
                updatedCourse.id(),
                updatedCourse.title(),
                updatedCourse.author(),
                updatedCourse.description(),
                updatedCourse.duration()));
  }

  @DeleteMapping("/{courseId}")
  public ResponseEntity<CourseGetResponse> deleteCourseForUser(
      Long userId, Long courseId) {
    var deletedCourse = courseService.deleteCourseForUser(userId, courseId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            new CourseGetResponse(
                deletedCourse.id(),
                deletedCourse.title(),
                deletedCourse.author(),
                deletedCourse.description(),
                deletedCourse.duration()));
  }
}

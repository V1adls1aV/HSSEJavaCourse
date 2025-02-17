package me.vladislav.homework02.app.api.route;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import me.vladislav.homework02.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework02.app.dto.api.request.CoursePatchRequest;
import me.vladislav.homework02.app.dto.api.request.CourseUpdateRequest;
import me.vladislav.homework02.app.dto.api.response.CourseGetResponse;
import me.vladislav.homework02.app.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/{userId}/course")
public class CourseHandler {
  private final CourseService courseService;

  public CourseHandler(CourseService courseService) {
    this.courseService = courseService;
  }

  @PostMapping
  public ResponseEntity<CourseGetResponse> addCourseForUser(
      @PathVariable Long userId, @Valid @RequestBody CourseCreateRequest course) {
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
  public ResponseEntity<List<CourseGetResponse>> getCoursesForUser(@PathVariable Long userId) {
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
      @PathVariable Long userId, @Valid @RequestBody CourseUpdateRequest course) {
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
      @PathVariable Long userId, @Valid @RequestBody CoursePatchRequest course) {
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
      @PathVariable Long userId, @PathVariable Long courseId) {
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

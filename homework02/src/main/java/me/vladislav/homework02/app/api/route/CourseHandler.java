package me.vladislav.homework02.app.api.route;

import jakarta.validation.Valid;
import me.vladislav.homework02.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework02.app.dto.api.response.CourseGetResponse;
import me.vladislav.homework02.app.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/{userId}/course")
public class CourseHandler {
  private final CourseService courseService;

  public CourseHandler(CourseService courseService) {
    this.courseService = courseService;
  }

  @PostMapping
  public ResponseEntity<Void> addCourseForUser(
      @PathVariable Long userId, @Valid @RequestBody CourseCreateRequest course) {
    courseService.addNewCourseForUser(userId, course);
    return ResponseEntity.status(HttpStatus.CREATED).build();
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
}

package me.vladislav.homework01.app.api.route;

import java.util.List;
import java.util.stream.Collectors;
import me.vladislav.homework01.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework01.app.dto.api.response.CourseGetResponse;
import me.vladislav.homework01.app.service.CourseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseHandler {
  private final CourseService courseService;

  public CourseHandler(CourseService courseService) {
    this.courseService = courseService;
  }

  @PutMapping("/user/{userId}")
  public void addCourseForUser(@PathVariable Long userId, @RequestBody CourseCreateRequest course) {
    courseService.addNewCourseForUser(userId, course);
  }

  @GetMapping("/user/{userId}")
  public List<CourseGetResponse> getCoursesForUser(@PathVariable Long userId) {
    return courseService.getCoursesForUser(userId).stream()
        .map(course -> new CourseGetResponse(
            course.id(),
            course.title(),
            course.author(),
            course.description(),
            course.duration()))
        .collect(Collectors.toList());
  }
}

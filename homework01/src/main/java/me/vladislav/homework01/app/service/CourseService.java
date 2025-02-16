package me.vladislav.homework01.app.service;

import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework01.app.db.repository.course.CourseRepository;
import me.vladislav.homework01.app.db.repository.user.UserRepository;
import me.vladislav.homework01.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework01.app.dto.service.Course;
import me.vladislav.homework01.app.dto.service.CourseData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseService {
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;

  public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
    this.courseRepository = courseRepository;
    this.userRepository = userRepository;
  }

  public void addNewCourseForUser(Long userId, CourseCreateRequest course) {
    log.info("Adding new course for user {}", userId);
    Long courseId = courseRepository.create(new CourseData(course.title(), course.author(),
        course.description(), course.duration()));
    userRepository.addCourseId(userId, courseId);
    log.info("Successfully added course with id {} for user {}", courseId, userId);
  }

  public List<Course> getCoursesForUser(Long userId) {
    log.info("Retrieving course for user {}", userId);
    Set<Long> courseIds = userRepository.getCoursesIds(userId);
    List<Course> courses = courseIds.parallelStream().map(courseRepository::getById).collect(Collectors.toList());
    log.info("Successfully retrieved course for user {}", userId);
    return courses;
  }
}
package me.vladislav.homework02.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework02.app.core.exception.db.repository.CourseNotFoundException;
import me.vladislav.homework02.app.db.repository.course.CourseRepository;
import me.vladislav.homework02.app.db.repository.user.UserRepository;
import me.vladislav.homework02.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework02.app.dto.api.request.CoursePatchRequest;
import me.vladislav.homework02.app.dto.api.request.CourseUpdateRequest;
import me.vladislav.homework02.app.dto.service.Course;
import me.vladislav.homework02.app.dto.service.CourseData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;

  public Course addNewCourseForUser(Long userId, CourseCreateRequest course) {
    log.info("Adding new course for user {}", userId);
    Long courseId = courseRepository.create(new CourseData(course.title(), course.author(),
        course.description(), course.duration()));
    userRepository.addCourseId(userId, courseId);
    log.info("Successfully added course with id {} for user {}", courseId, userId);
    return courseRepository.getById(courseId);
  }

  public List<Course> getCoursesForUser(Long userId) {
    log.info("Retrieving courses for user {}", userId);
    Set<Long> courseIds = userRepository.getCoursesIds(userId);
    List<Course> courses = courseIds.parallelStream()
        .map(courseRepository::getById)
        .collect(Collectors.toList());
    log.info("Successfully retrieved courses for user {}", userId);
    return courses;
  }

  public Course updateCourseForUser(Long userId, CourseUpdateRequest courseRequest) {
    log.info("Updating course {} for user {}", courseRequest.id(), userId);

    Set<Long> userCourseIds = userRepository.getCoursesIds(userId);
    if (!userCourseIds.contains(courseRequest.id())) {
      throw new CourseNotFoundException();
    }

    Course course = new Course(courseRequest.id(), courseRequest.title(), courseRequest.author(),
        courseRequest.description(), courseRequest.duration());
    Course updatedCourse = courseRepository.update(course);
    log.info("Successfully updated course {} for user {}", course.id(), userId);
    return updatedCourse;
  }

  public Course partiallyUpdateCourseForUser(Long userId, CoursePatchRequest courseRequest) {
    log.info("Partially updating course {} for user {}", courseRequest.id(), userId);

    Set<Long> userCourseIds = userRepository.getCoursesIds(userId);
    if (!userCourseIds.contains(courseRequest.id())) {
      throw new CourseNotFoundException();
    }

    Course existingCourse = courseRepository.getById(courseRequest.id());
    String newTitle = courseRequest.title() != null ? courseRequest.title() : existingCourse.title();
    String newAuthor = courseRequest.author() != null ? courseRequest.author() : existingCourse.author();
    String newDescription = courseRequest.description() != null ? courseRequest.description() : existingCourse.description();
    Integer newDuration = courseRequest.duration() != null ? courseRequest.duration() : existingCourse.duration();

    Course course = new Course(courseRequest.id(), newTitle, newAuthor, newDescription, newDuration);
    Course updatedCourse = courseRepository.update(course);
    log.info("Successfully partially updated course {} for user {}", course.id(), userId);
    return updatedCourse;
  }

  public Course deleteCourseForUser(Long userId, Long courseId) {
    log.info("Deleting course {} for user {}", courseId, userId);

    Set<Long> userCourseIds = userRepository.getCoursesIds(userId);
    if (!userCourseIds.contains(courseId)) {
      throw new CourseNotFoundException();
    }

    Course course = courseRepository.getById(courseId);
    courseRepository.delete(courseId);
    userCourseIds.remove(courseId);
    log.info("Successfully deleted course {} for user {}", courseId, userId);
    return course;
  }
}
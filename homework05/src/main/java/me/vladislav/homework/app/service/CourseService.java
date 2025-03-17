package me.vladislav.homework.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.core.exception.db.repository.CourseNotFoundException;
import me.vladislav.homework.app.core.exception.db.repository.UserNotFoundException;
import me.vladislav.homework.app.db.orm.Course;
import me.vladislav.homework.app.db.orm.User;
import me.vladislav.homework.app.db.repository.CourseRepository;
import me.vladislav.homework.app.db.repository.UserRepository;
import me.vladislav.homework.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework.app.dto.api.request.CoursePatchRequest;
import me.vladislav.homework.app.dto.api.request.CourseUpdateRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final Set<UUID> processedIds = ConcurrentHashMap.newKeySet();

  /*
   * Эта гарантия нужна для избежания дубликатов (создания курсов). Если же для обновления дубликат не критичен,
   * то для создания – может повлиять на логику работы приложения в худшую сторону.
   */
  @Transactional
  public Optional<Course> addNewCourseForUser(Long userId, CourseCreateRequest course) {
    log.info("Adding new course for user {}", userId);

    if (processedIds.add(course.operationId())) {

      Course savedCourse = courseRepository.save(new Course(null, course.title(), course.author(), course.description(), course.duration(), new User(userId)));

      log.info("Successfully added course with id {} for user {}", savedCourse.getId(), userId);
      return Optional.of(savedCourse);
    }
    return Optional.empty();
  }

  @Cacheable("CourseList")
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public List<Course> getCoursesForUser(Long userId) {
    log.info("Retrieving courses for user {}", userId);

    Set<Course> courses = courseRepository.findByUserId(userId);

    log.info("Successfully retrieved {} courses for user {}", courses.size(), userId);
    return courses.stream().toList();
  }

  @Transactional
  public Course updateCourseForUser(Long userId, CourseUpdateRequest courseRequest) {
    log.info("Updating course {} for user {}", courseRequest.id(), userId);

    Course course = courseRepository.save(
        new Course(
            courseRequest.id(), courseRequest.title(), courseRequest.author(),
            courseRequest.description(), courseRequest.duration(), new User(userId)
        )
    );

    log.info("Successfully updated course {} for user {}", course.getId(), userId);
    return course;
  }

  @Transactional
  public Course partiallyUpdateCourseForUser(Long userId, CoursePatchRequest courseRequest) {
    log.info("Partially updating course {} for user {}", courseRequest.id(), userId);

    Course course = courseRepository.findById(courseRequest.id()).orElseThrow(CourseNotFoundException::new);

    if (courseRequest.title() != null) course.setTitle(courseRequest.title());
    if (courseRequest.author() != null) course.setAuthor(courseRequest.author());
    if (courseRequest.description() != null) course.setDescription(courseRequest.description());
    if (courseRequest.duration() != null) course.setDuration(courseRequest.duration());

    course = courseRepository.save(course);
    log.info("Successfully partially updated course {} for user {}", course.getId(), userId);
    return course;
  }

  @Transactional
  public Course deleteCourseForUser(Long userId, Long courseId) {
    log.info("Deleting course {} for user {}", courseId, userId);

    Course course = courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
    courseRepository.delete(course);

    log.info("Successfully deleted course {} for user {}", courseId, userId);
    return course;
  }
}
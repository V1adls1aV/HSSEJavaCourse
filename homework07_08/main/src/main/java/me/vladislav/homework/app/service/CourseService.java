package me.vladislav.homework.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.api.route.broker.producer.AuditProducer;
import me.vladislav.homework.app.core.exception.db.repository.CourseNotFoundException;
import me.vladislav.homework.app.db.orm.Course;
import me.vladislav.homework.app.db.orm.User;
import me.vladislav.homework.app.db.repository.CourseRepository;
import me.vladislav.homework.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework.app.dto.api.request.CoursePatchRequest;
import me.vladislav.homework.app.dto.api.request.CourseUpdateRequest;
import me.vladislav.homework.app.dto.broker.OperationType;
import me.vladislav.homework.app.dto.broker.UserAuditData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {
  private final CourseRepository courseRepository;
  private final Set<UUID> processedIds = ConcurrentHashMap.newKeySet();
  private final AuditProducer auditProducer;

  /*
   * Эта гарантия нужна для избежания дубликатов (создания курсов). Если же для обновления дубликат не критичен,
   * то для создания – может повлиять на логику работы приложения в худшую сторону.
   */
  @Transactional
  public Optional<Course> addNewCourseForUser(Long userId, CourseCreateRequest course) {
    log.info("Adding new course for user {}", userId);

    if (processedIds.add(course.operationId())) {

      Course savedCourse =
          courseRepository.save(
              new Course(
                  null,
                  course.title(),
                  course.author(),
                  course.description(),
                  course.duration(),
                  new User(userId)));

      String message =
          String.format(
              "Successfully added course for user %d", userId);
      log.info(message);
      auditProducer.sendMessage(
          new UserAuditData(userId, LocalDateTime.now(), OperationType.CREATE, message));

      return Optional.of(savedCourse);
    }
    return Optional.empty();
  }

  @Cacheable("CourseList")
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public List<Course> getCoursesForUser(Long userId) {
    log.info("Retrieving courses for user {}", userId);

    Set<Course> courses = courseRepository.findByUserId(userId);

    String message =
        String.format("Successfully retrieved %d courses for user %d", courses.size(), userId);
    log.info(message);
    auditProducer.sendMessage(
        new UserAuditData(userId, LocalDateTime.now(), OperationType.READ, message));

    return courses.stream().toList();
  }

  @Transactional
  public Course updateCourseForUser(Long userId, CourseUpdateRequest courseRequest) {
    log.info("Updating course {} for user {}", courseRequest.id(), userId);

    Course course =
        courseRepository.save(
            new Course(
                courseRequest.id(),
                courseRequest.title(),
                courseRequest.author(),
                courseRequest.description(),
                courseRequest.duration(),
                new User(userId)));

    String message =
        String.format("Successfully updated course %d for user %d", course.getId(), userId);
    log.info(message);
    auditProducer.sendMessage(
        new UserAuditData(userId, LocalDateTime.now(), OperationType.UPDATE, message));

    return course;
  }

  @Transactional
  public Course partiallyUpdateCourseForUser(Long userId, CoursePatchRequest courseRequest) {
    log.info("Partially updating course {} for user {}", courseRequest.id(), userId);

    Course course =
        courseRepository.findById(courseRequest.id()).orElseThrow(CourseNotFoundException::new);

    if (courseRequest.title() != null) course.setTitle(courseRequest.title());
    if (courseRequest.author() != null) course.setAuthor(courseRequest.author());
    if (courseRequest.description() != null) course.setDescription(courseRequest.description());
    if (courseRequest.duration() != null) course.setDuration(courseRequest.duration());

    String message =
        String.format(
            "Successfully partially updated course %d for user %d", course.getId(), userId);
    log.info(message);
    auditProducer.sendMessage(
        new UserAuditData(userId, LocalDateTime.now(), OperationType.UPDATE, message));

    return course;
  }

  @Transactional
  public Course deleteCourseForUser(Long userId, Long courseId) {
    log.info("Deleting course {} for user {}", courseId, userId);

    Course course = courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
    courseRepository.delete(course);

    String message = String.format("Successfully deleted course %d for user %d", courseId, userId);
    log.info(message);
    auditProducer.sendMessage(
        new UserAuditData(userId, LocalDateTime.now(), OperationType.DELETE, message));

    return course;
  }
}

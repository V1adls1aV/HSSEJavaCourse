package me.vladislav.homework02.app.db.repository.course;

import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework02.app.core.exception.db.repository.UniversityNotFoundException;
import me.vladislav.homework02.app.dto.service.Course;
import me.vladislav.homework02.app.dto.service.CourseData;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class InMemoryCourseRepository implements CourseRepository {
  private static final Map<Long, Course> storage = new ConcurrentHashMap<>();
  private static final AtomicLong idGenerator = new AtomicLong(1L);

  public InMemoryCourseRepository() {
  }

  public Long create(CourseData course) {
    log.debug("Creating new course: {}", course);
    Long id = idGenerator.getAndIncrement();
    storage.put(id, new Course(id, course.title(), course.author(), course.description(), course.duration()));
    log.debug("Created book with id: {}", id);
    return id;
  }

  public Course getById(Long id) {
    log.debug("Retrieving book with id: {}", id);
    Course course = storage.get(id);
    if (course == null) {
      throw new UniversityNotFoundException();
    }
    log.debug("Retrieved book with id: {}", id);
    return course;
  }
}
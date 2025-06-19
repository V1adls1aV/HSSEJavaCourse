package me.vladislav.homework.app.db.repository.course;

import me.vladislav.homework.app.dto.service.Course;
import me.vladislav.homework.app.dto.service.CourseData;

public interface CourseRepository {
  Long create(CourseData course);

  Course getById(Long id);

  Course update(Course course);

  void delete(Long id);
}
package me.vladislav.homework02.app.db.repository.course;

import me.vladislav.homework02.app.dto.service.Course;
import me.vladislav.homework02.app.dto.service.CourseData;

public interface CourseRepository {
  Long create(CourseData course);

  Course getById(Long id);
}
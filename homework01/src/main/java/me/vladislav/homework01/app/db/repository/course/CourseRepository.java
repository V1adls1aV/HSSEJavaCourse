package me.vladislav.homework01.app.db.repository.course;

import me.vladislav.homework01.app.dto.service.Course;
import me.vladislav.homework01.app.dto.service.CourseData;

public interface CourseRepository {
  Long create(CourseData course);

  Course getById(Long id);
}
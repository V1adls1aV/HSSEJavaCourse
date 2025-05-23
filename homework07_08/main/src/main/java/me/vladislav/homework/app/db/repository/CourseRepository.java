package me.vladislav.homework.app.db.repository;

import me.vladislav.homework.app.db.orm.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {
  Set<Course> findByUserId(Long userId);
}

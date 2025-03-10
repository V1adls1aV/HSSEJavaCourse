package me.vladislav.homework.app.db.repository;

import me.vladislav.homework.app.db.orm.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

package me.vladislav.homework01.app.db.repository.course;

import me.vladislav.homework01.app.core.exception.db.repository.UniversityNotFoundException;
import me.vladislav.homework01.app.dto.service.Course;
import me.vladislav.homework01.app.dto.service.CourseData;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCourseRepository implements CourseRepository {
    private static final Map<Long, Course> storage = new ConcurrentHashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(1L);

    public InMemoryCourseRepository() {}

    public Long create(CourseData course) {
        Long id = idGenerator.getAndIncrement();
        storage.put(id, new Course(id, course.title(), course.author(), course.description(), course.duration()));
        return id;
    }

    public Course getById(Long id) {
        Course course = storage.get(id);
        if (course == null) {
            throw new UniversityNotFoundException();
        }
        return course;
    }
}
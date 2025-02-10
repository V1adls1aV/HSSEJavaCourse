package me.vladislav.homework01.app.service;

import me.vladislav.homework01.app.db.repository.course.CourseRepository;
import me.vladislav.homework01.app.db.repository.user.UserRepository;
import me.vladislav.homework01.app.dto.api.request.CourseCreateRequest;
import me.vladislav.homework01.app.dto.service.Course;
import me.vladislav.homework01.app.dto.service.CourseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public void addNewCourseForUser(Long userId, CourseCreateRequest course) {
        Long courseId = courseRepository.create(new CourseData(course.title(), course.author(),
            course.description(), course.duration()));
        userRepository.addCourseId(userId, courseId);
    }

    public List<Course> getCoursesForUser(Long userId) {
        Set<Long> courseIds = userRepository.getCoursesIds(userId);
        return courseIds.parallelStream().map(courseRepository::getById).collect(Collectors.toList());
    }
}
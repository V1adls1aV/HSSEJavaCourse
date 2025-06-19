package me.vladislav.homework02.app.db.repository.user;

import java.util.Set;
import me.vladislav.homework02.app.dto.service.User;
import me.vladislav.homework02.app.dto.service.UserData;

public interface UserRepository {
  Long create(UserData user);

  User getById(Long id);

  void addBookId(Long userId, Long bookId);

  Set<Long> getBooksIds(Long userId);

  void addCourseId(Long userId, Long courseId);

  Set<Long> getCoursesIds(Long userId);

  void addUniversityId(Long userId, Long universityId);

  Set<Long> getUniversitiesIds(Long userId);
}

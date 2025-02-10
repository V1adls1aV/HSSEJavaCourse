package me.vladislav.homework01.app.db.repository.user;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework01.app.core.exception.db.repository.UserNotFoundException;
import me.vladislav.homework01.app.dto.service.User;
import me.vladislav.homework01.app.dto.service.UserData;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class InMemoryUserRepository implements UserRepository {
  private static final Map<Long, User> storage = new ConcurrentHashMap<>();
  private static final Map<Long, Set<Long>> bookIds = new ConcurrentHashMap<>();
  private static final Map<Long, Set<Long>> courseIds = new ConcurrentHashMap<>();
  private static final Map<Long, Set<Long>> universityIds = new ConcurrentHashMap<>();
  private static final AtomicLong idGenerator = new AtomicLong(1L);

  public InMemoryUserRepository() {}

  public Long create(UserData user) {
    log.debug("Creating new user: {}", user.username());
    Long id = idGenerator.getAndIncrement();
    storage.put(id, new User(id, user.username(), user.email()));
    log.debug("Created user with id: {}", id);
    return id;
  }

  public User getById(Long id) {
    log.debug("Retrieving user with id: {}", id);
    User user = storage.get(id);
    if (user == null) {
      log.error("User not found with id: {}", id);
      throw new UserNotFoundException();
    }
    log.debug("Found user: {}", user.username());
    return user;
  }

  public void addBookId(Long userId, Long bookId) {
    log.debug("Adding book {} to user {}", bookId, userId);
    if (!storage.containsKey(userId)) {
      log.error("User not found with id: {}", userId);
      throw new UserNotFoundException();
    }

    if (!bookIds.containsKey(userId)) {
      Set<Long> books = new ConcurrentSkipListSet<>();
      books.add(bookId);
      bookIds.put(userId, books);
    } else {
      bookIds.get(userId).add(bookId);
    }
    log.debug("Successfully added book {} to user {}", bookId, userId);
  }

  public Set<Long> getBooksIds(Long userId) {
    log.debug("Retrieving books for user: {}", userId);
    if (!storage.containsKey(userId)) {
      log.error("User not found with id: {}", userId);
      throw new UserNotFoundException();
    }
    Set<Long> books = bookIds.get(userId);
    if (books == null) {
      log.debug("No books found for user: {}", userId);
      return Set.of();
    }
    log.debug("Found {} books for user {}", books.size(), userId);
    return books;
  }

  public void addCourseId(Long userId, Long courseId) {
    if (!storage.containsKey(userId)) {
      throw new UserNotFoundException();
    }

    if (!courseIds.containsKey(userId)) {
      Set<Long> courses = new ConcurrentSkipListSet<>();
      courses.add(courseId);
      courseIds.put(userId, courses);
    } else {
      courseIds.get(userId).add(courseId);
    }
  }

  public Set<Long> getCoursesIds(Long userId) {
    if (!storage.containsKey(userId)) {
      throw new UserNotFoundException();
    }
    Set<Long> courses = courseIds.get(userId);
    if (courses == null) {
      return Set.of();
    }
    return courses;
  }

  public void addUniversityId(Long userId, Long universityId) {
    if (!storage.containsKey(userId)) {
      throw new UserNotFoundException();
    }

    if (!universityIds.containsKey(userId)) {
      Set<Long> universities = new ConcurrentSkipListSet<>();
      universities.add(universityId);
      universityIds.put(userId, universities);
    } else {
      universityIds.get(userId).add(universityId);
    }
  }

  public Set<Long> getUniversitiesIds(Long userId) {
    if (!storage.containsKey(userId)) {
      throw new UserNotFoundException();
    }
    Set<Long> universities = universityIds.get(userId);
    if (universities == null) {
      return Set.of();
    }
    return universities;
  }
}

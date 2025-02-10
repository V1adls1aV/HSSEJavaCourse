package me.vladislav.homework01.app.db.repository.user;

import me.vladislav.homework01.app.core.exception.db.repository.UserNotFoundException;
import me.vladislav.homework01.app.dto.service.User;
import me.vladislav.homework01.app.dto.service.UserData;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserRepository implements UserRepository {
  private static final Map<Long, User> storage = new ConcurrentHashMap<>();
  private static final Map<Long, Set<Long>> bookIds = new ConcurrentHashMap<>();
  private static final Map<Long, Set<Long>> courseIds = new ConcurrentHashMap<>();
  private static final Map<Long, Set<Long>> universityIds = new ConcurrentHashMap<>();
  private static final AtomicLong idGenerator = new AtomicLong(1L);

  public InMemoryUserRepository() {

  }

  public Long create(UserData user) {
    Long id = idGenerator.getAndIncrement();
    storage.put(id, new User(id, user.username(), user.email()));
    return id;
  }

  public User getById(Long id) {
    User user = storage.get(id);
    if (user == null) {
      throw new UserNotFoundException();
    }
    return user;
  }

  public void addBookId(Long userId, Long bookId) {
    if (!storage.containsKey(userId)) {
      throw new UserNotFoundException();
    }

    if (!bookIds.containsKey(userId)) {
      Set<Long> books = new ConcurrentSkipListSet<>();
      books.add(bookId);
      bookIds.put(userId, books);
    } else {
      bookIds.get(userId).add(bookId);
    }
  }

  public Set<Long> getBooksIds(Long userId) {
    if (!storage.containsKey(userId)) {
      throw new UserNotFoundException();
    }
    Set<Long> books = bookIds.get(userId);
    if (books == null) {return Set.of();}
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

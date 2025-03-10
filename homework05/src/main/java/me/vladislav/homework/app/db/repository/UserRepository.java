package me.vladislav.homework.app.db.repository;

import me.vladislav.homework.app.db.orm.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
  @Query("SELECT u FROM User u LEFT JOIN FETCH u.books WHERE u.id = :userId")
  User findUserWithBooks(@Param("userId") Long userId);

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.courses WHERE u.id = :userId")
  User findUserWithCourses(@Param("userId") Long userId);

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.universities WHERE u.id = :userId")
  User findUserWithUniversities(@Param("userId") Long userId);
}

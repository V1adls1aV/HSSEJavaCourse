package me.vladislav.homework.app.db.repository;

import me.vladislav.homework.app.db.orm.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UniversityRepository extends JpaRepository<University, Long> {
  Set<University> findByUserId(Long userId);
}

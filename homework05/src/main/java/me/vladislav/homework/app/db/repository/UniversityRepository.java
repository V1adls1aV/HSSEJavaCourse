package me.vladislav.homework.app.db.repository;

import me.vladislav.homework.app.db.orm.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {

}

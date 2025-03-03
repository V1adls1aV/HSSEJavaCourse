package me.vladislav.homework.app.db.repository.university;

import me.vladislav.homework.app.dto.service.University;
import me.vladislav.homework.app.dto.service.UniversityData;

public interface UniversityRepository {
  Long create(UniversityData university);

  University getById(Long id);
}
package me.vladislav.homework01.app.db.repository.university;

import me.vladislav.homework01.app.dto.service.University;
import me.vladislav.homework01.app.dto.service.UniversityData;

public interface UniversityRepository {
  Long create(UniversityData university);

  University getById(Long id);
}
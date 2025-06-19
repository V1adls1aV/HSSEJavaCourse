package me.vladislav.homework02.app.db.repository.university;

import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework02.app.core.exception.db.repository.UniversityNotFoundException;
import me.vladislav.homework02.app.dto.service.University;
import me.vladislav.homework02.app.dto.service.UniversityData;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class InMemoryUniversityRepository implements UniversityRepository {
  private static final Map<Long, University> storage = new ConcurrentHashMap<>();
  private static final AtomicLong idGenerator = new AtomicLong(1L);

  public InMemoryUniversityRepository() {
  }

  public Long create(UniversityData university) {
    log.debug("Creating university: {}", university);
    Long id = idGenerator.getAndIncrement();
    storage.put(id, new University(id, university.name(), university.city(),
        university.description(), university.rateKrutosty()));
    log.debug("Created university with id: {}", id);
    return id;
  }

  public University getById(Long id) {
    log.debug("Retrieving book with id: {}", id);
    University university = storage.get(id);
    if (university == null) {
      throw new UniversityNotFoundException();
    }
    log.debug("Retrieved book with id: {}", id);
    return university;
  }
}
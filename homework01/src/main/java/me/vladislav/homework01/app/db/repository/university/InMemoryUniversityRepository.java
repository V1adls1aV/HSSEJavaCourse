package me.vladislav.homework01.app.db.repository.university;

import me.vladislav.homework01.app.core.exception.db.repository.UniversityNotFoundException;
import me.vladislav.homework01.app.dto.service.University;
import me.vladislav.homework01.app.dto.service.UniversityData;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUniversityRepository implements UniversityRepository {
    private static final Map<Long, University> storage = new ConcurrentHashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(1L);

    public InMemoryUniversityRepository() {}

    public Long create(UniversityData university) {
        Long id = idGenerator.getAndIncrement();
        storage.put(id, new University(id, university.name(), university.city(),
            university.description(), university.rateKrutosty()));
        return id;
    }

    public University getById(Long id) {
        University university = storage.get(id);
        if (university == null) {
            throw new UniversityNotFoundException();
        }
        return university;
    }
}
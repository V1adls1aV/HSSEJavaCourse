package me.vladislav.homework.app.db.repository.university;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.core.exception.db.repository.UniversityNotFoundException;
import me.vladislav.homework.app.dto.service.University;
import me.vladislav.homework.app.dto.service.UniversityData;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryUniversityRepository implements UniversityRepository {
  private static final Map<Long, University> storage = new ConcurrentHashMap<>();
  private static final AtomicLong idGenerator = new AtomicLong(1L);
  private final RestTemplate restTemplate;
  private final WebClient webClient;

  public Long create(UniversityData university) {
    log.debug("Creating university: {}", university);
    Long id = idGenerator.getAndIncrement();
    storage.put(
        id,
        new University(
            id,
            university.name(),
            university.city(),
            university.description(),
            university.rateKrutosty()));

    // WebClient get request
    webClient
        .get()
        .uri("https://github.com/V1adls1aV/HSSEJavaCourse")
        .retrieve()
        .bodyToMono(String.class)
        .block();

    log.debug("Created university with id: {}", id);
    return id;
  }

  public University getById(Long id) {
    log.debug("Retrieving book with id: {}", id);
    University university = storage.get(id);
    if (university == null) {
      throw new UniversityNotFoundException();
    }

    // Rest Template
    restTemplate.getForEntity("https://github.com/V1adls1aV/HSSEJavaCourse", String.class);

    log.debug("Retrieved book with id: {}", id);
    return university;
  }
}

package me.vladislav.homework.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.db.repository.UniversityRepository;
import me.vladislav.homework.app.db.repository.UserRepository;
import me.vladislav.homework.app.dto.api.request.UniversityCreateRequest;
import me.vladislav.homework.app.db.orm.University;
import me.vladislav.homework.app.dto.service.UniversityData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityService {
  private final UserRepository userRepository;
  private final UniversityRepository universityRepository;

  public void addNewUniversityForUser(Long userId, UniversityCreateRequest university) {
    log.info("Adding new university for user {}", userId);
    Long universityId = universityRepository.create(new UniversityData(university.name(),
        university.city(), university.description(), university.rateKrutosty()));
    userRepository.addUniversityId(userId, universityId);
    log.info("Successfully added university for user {}", userId);
  }

  @Cacheable("UniversityList")
  public List<University> getUniversitiesForUser(Long userId) {
    log.info("Retrieving universities for user {}", userId);
    Set<Long> universityIds = userRepository.getUniversitiesIds(userId);
    List<University> universities = universityIds.parallelStream()
        .map(universityRepository::getById)
        .toList();
    log.info("Successfully retrieved universities for user {}", userId);
    return universities;
  }
}
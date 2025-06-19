package me.vladislav.homework02.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework02.app.db.repository.university.UniversityRepository;
import me.vladislav.homework02.app.db.repository.user.UserRepository;
import me.vladislav.homework02.app.dto.api.request.UniversityCreateRequest;
import me.vladislav.homework02.app.dto.service.University;
import me.vladislav.homework02.app.dto.service.UniversityData;
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
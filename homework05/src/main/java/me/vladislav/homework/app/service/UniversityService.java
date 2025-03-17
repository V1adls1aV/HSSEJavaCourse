package me.vladislav.homework.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.db.orm.University;
import me.vladislav.homework.app.db.orm.User;
import me.vladislav.homework.app.db.repository.UniversityRepository;
import me.vladislav.homework.app.db.repository.UserRepository;
import me.vladislav.homework.app.dto.api.request.UniversityCreateRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityService {
  private final UserRepository userRepository;
  private final UniversityRepository universityRepository;

  @Transactional
  public void addNewUniversityForUser(Long userId, UniversityCreateRequest university) {
    log.info("Adding new university for user {}", userId);

    University newUniversity = universityRepository.save(
        new University(null, university.name(), university.city(),
            university.description(), university.rateKrutosty(), new User(userId)));

    log.info("Successfully added university for user {}", userId);
  }

  @Cacheable("UniversityList")
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public List<University> getUniversitiesForUser(Long userId) {
    log.info("Retrieving universities for user {}", userId);
    Set<University> universities = universityRepository.findByUserId(userId);
    log.info("Successfully retrieved universities for user {}", userId);
    return universities.stream().toList();
  }
}
package me.vladislav.homework.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.homework.app.api.route.broker.producer.AuditProducer;
import me.vladislav.homework.app.db.orm.University;
import me.vladislav.homework.app.db.orm.User;
import me.vladislav.homework.app.db.repository.UniversityRepository;
import me.vladislav.homework.app.dto.api.request.UniversityCreateRequest;
import me.vladislav.homework.app.dto.broker.OperationType;
import me.vladislav.homework.app.dto.broker.UserAuditData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityService {
  private final UniversityRepository universityRepository;
  private final AuditProducer auditProducer;

  @Transactional
  public void addNewUniversityForUser(Long userId, UniversityCreateRequest university) {
    log.info("Adding new university for user {}", userId);

    University newUniversity =
        universityRepository.save(
            new University(
                null,
                university.name(),
                university.city(),
                university.description(),
                university.rateKrutosty(),
                new User(userId)));

    String message = String.format("Successfully added university for user %d", userId);
    log.info(message);
    auditProducer.sendMessage(
        new UserAuditData(userId, LocalDateTime.now(), OperationType.CREATE, message));
  }

  @Cacheable("UniversityList")
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public List<University> getUniversitiesForUser(Long userId) {
    log.info("Retrieving universities for user {}", userId);
    Set<University> universities = universityRepository.findByUserId(userId);

    String message = String.format("Successfully retrieved universities for user %d", userId);
    log.info(message);
    auditProducer.sendMessage(
        new UserAuditData(userId, LocalDateTime.now(), OperationType.READ, message));

    return universities.stream().toList();
  }
}

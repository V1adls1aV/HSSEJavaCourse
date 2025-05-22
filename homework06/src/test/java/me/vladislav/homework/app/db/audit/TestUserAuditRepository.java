package me.vladislav.homework.app.db.audit;

import me.vladislav.homework.app.db.audit.entity.UserAudit;
import me.vladislav.homework.app.db.audit.entity.UserAuditKey;
import me.vladislav.homework.app.db.audit.repository.UserAuditRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class TestUserAuditRepository {

  @Container
  private static final CassandraContainer<?> CASSANDRA =
      new CassandraContainer<>("cassandra:4.1.9").withExposedPorts(9042);
  @Autowired
  private UserAuditRepository userAuditRepository;

  @BeforeAll
  static void setupCassandraConnectionProperties() {
    System.setProperty("spring.cassandra.port", String.valueOf(CASSANDRA.getMappedPort(9042)));
  }

  @Test
  public void testSaveAndLoadAudit() {
    LocalDateTime fixedTime = LocalDateTime.of(2025, 5, 22, 17, 41, 57, 0);
    UserAuditKey userAuditKey = new UserAuditKey(1L, fixedTime);
    UserAudit audit = new UserAudit(userAuditKey, "CREATE", "User was created");

    userAuditRepository.save(audit);

    UserAudit newAudit = userAuditRepository.findByUserIdAndPerformTime(userAuditKey.getUserId(), userAuditKey.getPerformTime());

    assertEquals(audit.getKey(), newAudit.getKey());
    assertEquals(audit.getOperationType(), newAudit.getOperationType());
    assertEquals(audit.getDetail(), newAudit.getDetail());
  }
}
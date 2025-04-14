package me.vladislav.homework.app.db.audit;

import me.vladislav.homework.app.TestContainersConfig;
import me.vladislav.homework.app.db.audit.entity.UserAudit;
import me.vladislav.homework.app.db.audit.entity.UserAuditKey;
import me.vladislav.homework.app.db.audit.repository.UserAuditRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
public class TestUserAuditRepository extends TestContainersConfig {

  @Autowired
  private UserAuditRepository userAuditRepository;

  @Test
  public void testSaveAndLoadAudit() {
    UserAuditKey userAuditKey = new UserAuditKey(1L, LocalDateTime.now());
    UserAudit audit = new UserAudit(userAuditKey, "CREATE", "User was created");

    userAuditRepository.save(audit);

    UserAudit newAudit = userAuditRepository.findByKey(userAuditKey);

    assertEquals(audit.getKey(), newAudit.getKey());
    assertEquals(audit.getOperationType(), newAudit.getOperationType());
    assertEquals(audit.getDetail(), newAudit.getDetail());
  }
}
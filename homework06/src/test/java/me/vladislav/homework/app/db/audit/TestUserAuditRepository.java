package me.vladislav.homework.app.db.audit;

import me.vladislav.homework.app.TestContainersManager;
import me.vladislav.homework.app.db.audit.entity.UserAudit;
import me.vladislav.homework.app.db.audit.entity.UserAuditKey;
import me.vladislav.homework.app.db.audit.repository.UserAuditRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestUserAuditRepository extends TestContainersManager {
  @Autowired
  private UserAuditRepository userAuditRepository;

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
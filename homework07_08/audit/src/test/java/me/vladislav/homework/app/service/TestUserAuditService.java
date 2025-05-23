package me.vladislav.homework.app.service;

import me.vladislav.homework.app.TestContainersManager;
import me.vladislav.homework.app.db.audit.entity.UserAudit;
import me.vladislav.homework.app.db.audit.entity.UserAuditKey;
import me.vladislav.homework.app.db.audit.repository.UserAuditRepository;
import me.vladislav.homework.app.dto.OperationType;
import me.vladislav.homework.app.dto.UserAuditData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestUserAuditService extends TestContainersManager {

  private final Long testUserId = 1L;
  private final Long nonExistentUserId = 999L;

  @Autowired
  private UserAuditRepository userAuditRepository;

  @Autowired
  private UserAuditService userAuditService;

  @Test
  public void testSaveAndLoadAuditUsingRepository() {
    LocalDateTime fixedTime = LocalDateTime.of(2025, 5, 22, 17, 41, 57, 0);
    UserAuditKey userAuditKey = new UserAuditKey(234L, fixedTime);
    UserAudit audit = new UserAudit(userAuditKey, "CREATE", "User was created");

    userAuditRepository.save(audit);

    UserAudit newAudit = userAuditRepository.findByUserIdAndPerformTime(userAuditKey.getUserId(), userAuditKey.getPerformTime());

    assertEquals(audit.getKey(), newAudit.getKey());
    assertEquals(audit.getOperationType(), newAudit.getOperationType());
    assertEquals(audit.getDetail(), newAudit.getDetail());
  }

  @Test
  public void testEmptyRecordList() {
    List<UserAuditData> result = userAuditService.getUserAuditData(nonExistentUserId, 10);

    assertTrue(result.isEmpty(), "Expected empty list for user without records");
  }

  @Test
  public void testRecordListLimiting() {
    LocalDateTime now = LocalDateTime.now();

    // Shuffle saving order
    userAuditService.recordUserAction(new UserAuditData(testUserId, now.minusHours(1), OperationType.CREATE, "Book with book_id=123 was created"));
    userAuditService.recordUserAction(new UserAuditData(testUserId, now, OperationType.READ, "Book with book_id=123 was read"));
    userAuditService.recordUserAction(new UserAuditData(testUserId, now.minusHours(2), OperationType.UPDATE, "Book with book_id=123 was modified"));

    // Test top 2
    List<UserAuditData> firstResult = userAuditService.getUserAuditData(testUserId, 2);
    assertEquals(2, firstResult.size(), "First query should have 2 records");

    // Test top 4
    List<UserAuditData> secondResult = userAuditService.getUserAuditData(testUserId, 4);
    assertEquals(3, secondResult.size(), "Second query should have 3 records out of 4");
    assertEquals(OperationType.READ, firstResult.get(0).operationType(), "First record was added last.");
    assertEquals(OperationType.CREATE, firstResult.get(1).operationType(), "Second record was added 1 record before the last.");
    assertEquals(OperationType.UPDATE, secondResult.get(2).operationType(), "Third record should be the oldest");
  }
}

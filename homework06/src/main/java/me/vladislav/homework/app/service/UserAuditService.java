package me.vladislav.homework.app.service;

import java.util.List;
import me.vladislav.homework.app.db.audit.entity.UserAudit;
import me.vladislav.homework.app.db.audit.entity.UserAuditKey;
import me.vladislav.homework.app.db.audit.repository.UserAuditRepository;
import me.vladislav.homework.app.dto.service.OperationType;
import me.vladislav.homework.app.dto.service.UserAuditData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuditService {

  @Autowired private UserAuditRepository userAuditRepository;

  public void recordUserAction(UserAuditData auditData) {
    UserAuditKey userAuditKey = new UserAuditKey(auditData.userId(), auditData.performTime());
    UserAudit audit =
        new UserAudit(userAuditKey, auditData.operationType().toString(), auditData.toString());

    userAuditRepository.save(audit);
  }

  public List<UserAuditData> getUserAuditData(Long userId, int limit) {
    List<UserAudit> audits = userAuditRepository.findAllByUserId(userId, limit);
    return audits.stream()
        .map(
            audit ->
                new UserAuditData(
                    audit.getKey().getUserId(),
                    audit.getKey().getPerformTime(),
                    OperationType.valueOf(audit.getOperationType()),
                    audit.getDetail()))
        .toList();
  }
}

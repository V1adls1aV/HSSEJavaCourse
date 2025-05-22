package me.vladislav.homework.app.db.audit.repository;

import jakarta.validation.constraints.NotNull;
import me.vladislav.homework.app.db.audit.entity.UserAudit;
import me.vladislav.homework.app.db.audit.entity.UserAuditKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import java.time.LocalDateTime;

public interface UserAuditRepository extends CassandraRepository<UserAudit, UserAuditKey> {
  @Query("SELECT * FROM user_audit WHERE user_id = :userId AND perform_time = :performTime")
  UserAudit findByUserIdAndPerformTime(@NotNull Long userId, @NotNull LocalDateTime performTime);
}

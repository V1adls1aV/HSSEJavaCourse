package me.vladislav.homework.app.db.audit.repository;

import jakarta.validation.constraints.NotNull;
import me.vladislav.homework.app.db.audit.entity.UserAudit;
import me.vladislav.homework.app.db.audit.entity.UserAuditKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface UserAuditRepository extends CassandraRepository<UserAudit, UserAuditKey> {
  UserAudit findByKey(@NotNull UserAuditKey key);
}

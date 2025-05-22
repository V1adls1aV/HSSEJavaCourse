package me.vladislav.homework.app.db.audit.repository;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import me.vladislav.homework.app.db.audit.entity.UserAudit;
import me.vladislav.homework.app.db.audit.entity.UserAuditKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface UserAuditRepository extends CassandraRepository<UserAudit, UserAuditKey> {
  /**
   * Find a specific audit record by user ID and perform time.
   *
   * @param userId The ID of the user to find audit records for
   * @param performTime The time at which the audit record was performed
   * @return The UserAudit record for the specified user and perform time
   */
  @Query("SELECT * FROM user_audit WHERE user_id = :userId AND perform_time = :performTime")
  UserAudit findByUserIdAndPerformTime(@NotNull Long userId, @NotNull LocalDateTime performTime);

  /**
   * Find all audit records for a specific user with. Since user_id is the partition key and
   * perform_time is clustered with DESC order, we can use LIMIT to get the most recent records.
   *
   * @param userId The ID of the user to find audit records for
   * @param limit The maximum number of records to return
   * @return A list of UserAudit records for the specified user
   */
  @Query("SELECT * FROM user_audit WHERE user_id = :userId LIMIT :limit")
  List<UserAudit> findAllByUserId(@NotNull Long userId, int limit);
}

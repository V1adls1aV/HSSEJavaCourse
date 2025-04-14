package me.vladislav.homework.app.db.audit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@PrimaryKeyClass
public class UserAuditKey implements Serializable {
  @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
  private Long userId;

  @PrimaryKeyColumn(name = "perform_time", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
  private LocalDateTime performTime;
}

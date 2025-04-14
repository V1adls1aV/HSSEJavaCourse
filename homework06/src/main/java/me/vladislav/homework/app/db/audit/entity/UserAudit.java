package me.vladislav.homework.app.db.audit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "user_audit")
public class UserAudit {
    @PrimaryKey
    private UserAuditKey key;

    @Column(value = "operation_type")
    private String operationType;

    @Column()
    private String detail;
}
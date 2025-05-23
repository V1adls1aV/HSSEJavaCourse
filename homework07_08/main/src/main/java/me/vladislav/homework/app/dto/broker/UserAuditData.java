package me.vladislav.homework.app.dto.broker;

import java.time.LocalDateTime;

public record UserAuditData(
    Long userId, LocalDateTime performTime, OperationType operationType, String detail) {}

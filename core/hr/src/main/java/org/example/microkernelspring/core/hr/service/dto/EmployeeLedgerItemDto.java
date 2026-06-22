package org.example.microkernelspring.core.hr.service.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EmployeeLedgerItemDto(
        UUID id,
        UUID tenantId,
        UUID employeeId,
        String entryTypeCode,
        BigDecimal amount,
        Instant createdAt,
        String reason
) {
}
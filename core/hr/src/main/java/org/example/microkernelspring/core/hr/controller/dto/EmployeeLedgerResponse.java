package org.example.microkernelspring.core.hr.controller.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EmployeeLedgerResponse(
        UUID id,
        UUID tenantId,
        UUID employeeId,
        String entryTypeCode,
        BigDecimal amount,
        Instant effectiveDate,
        String description
) {
}
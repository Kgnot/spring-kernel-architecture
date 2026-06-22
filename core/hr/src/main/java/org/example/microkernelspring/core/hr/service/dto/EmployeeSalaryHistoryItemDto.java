package org.example.microkernelspring.core.hr.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeSalaryHistoryItemDto(
        UUID id,
        UUID tenantId,
        UUID employeeId,
        BigDecimal salary,
        String currency,
        LocalDate effectiveDate,
        String reason
) {
}
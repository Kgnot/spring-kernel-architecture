package org.example.microkernelspring.core.hr.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeSalaryHistoryResponse(
        UUID id,
        UUID tenantId,
        UUID employeeId,
        BigDecimal salary,
        String currency,
        LocalDate effectiveDate,
        String reason
) {
}
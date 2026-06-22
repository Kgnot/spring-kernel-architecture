package org.example.microkernelspring.core.hr.service.dto;

import java.time.LocalDate;
import java.util.UUID;

public record EmployeeLedgerFilterDto(
        UUID tenantId,
        UUID employeeId,
        LocalDate from,
        LocalDate to,
        String entryTypeCode
) {
}
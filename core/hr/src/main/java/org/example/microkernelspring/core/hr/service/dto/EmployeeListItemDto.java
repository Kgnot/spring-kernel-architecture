package org.example.microkernelspring.core.hr.service.dto;

import java.util.UUID;

public record EmployeeListItemDto(
        UUID id,
        UUID tenantId,
        String firstName,
        String lastName,
        String employmentStatusCode
) {
}
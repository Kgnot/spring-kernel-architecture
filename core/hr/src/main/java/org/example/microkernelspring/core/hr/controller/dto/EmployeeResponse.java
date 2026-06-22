package org.example.microkernelspring.core.hr.controller.dto;

import java.util.UUID;

public record EmployeeResponse(
        UUID id,
        UUID tenantId,
        String firstName,
        String lastName,
        String employmentStatusCode
) {
}
package org.example.microkernelspring.core.identity.controller.dto;

import java.util.UUID;

public record RegisterResponse(
        UUID userLoginId,
        UUID profileId,
        UUID tenantId,
        String email,
        String fullName
) {
}

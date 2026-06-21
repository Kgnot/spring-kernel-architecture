package org.example.microkernelspring.core.identity.application.usecase.result;

import java.util.UUID;

public record RegisterUserResult(
        UUID userLoginId,
        UUID profileId,
        UUID tenantId,
        String email,
        String fullName
) {
}
package org.example.microkernelspring.core.identity.api.dto;

import java.time.Instant;
import java.util.UUID;

public record CustomerProfileDetails(
        UUID profileId,
        String avatarUrl,
        String firstName,
        String lastName,
        String documentNumber,
        Instant createdAt
) {
}

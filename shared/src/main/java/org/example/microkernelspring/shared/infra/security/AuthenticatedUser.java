package org.example.microkernelspring.shared.infra.security;

import java.util.List;
import java.util.UUID;

public record AuthenticatedUser(
        UUID userId,
        UUID tenantId,
        String email,
        List<String> roles
) {
}

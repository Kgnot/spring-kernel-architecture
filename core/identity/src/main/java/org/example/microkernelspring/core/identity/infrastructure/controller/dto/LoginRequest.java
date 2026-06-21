package org.example.microkernelspring.core.identity.infrastructure.controller.dto;

public record LoginRequest(
        String subdomain,
        String email,
        String password
) {
}

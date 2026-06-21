package org.example.microkernelspring.core.identity.infrastructure.controller.dto;


public record RegisterRequest(
        String subdomain,
        String email,
        String password,
        String firstName,
        String lastName
) {
}

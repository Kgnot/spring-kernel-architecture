package org.example.microkernelspring.core.identity.application.usecase.command;

public record RegisterUserCommand(
        String subdomain,
        String firstName,
        String lastName,
        String email,
        String password
) {
}
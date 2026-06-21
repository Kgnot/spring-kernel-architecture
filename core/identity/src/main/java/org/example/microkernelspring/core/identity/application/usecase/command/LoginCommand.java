package org.example.microkernelspring.core.identity.application.usecase.command;

public record LoginCommand(
        String subdomain,
        String email,
        String password
) {
}
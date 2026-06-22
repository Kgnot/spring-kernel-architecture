package org.example.microkernelspring.core.identity.application.usecase.result;

public record RefreshResult(
        String accessToken,
        String refreshToken
) {}
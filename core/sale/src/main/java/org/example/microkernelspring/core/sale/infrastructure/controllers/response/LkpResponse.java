package org.example.microkernelspring.core.sale.infrastructure.controllers.response;

import java.util.UUID;

public record LkpResponse(UUID id, String code, String name) {}
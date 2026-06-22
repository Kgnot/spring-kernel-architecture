package org.example.microkernelspring.core.stock.controller.response;

import java.util.UUID;

public record LkpMovementTypeResponse(
        UUID id,
        String code,
        String name,
        short direction
) {}

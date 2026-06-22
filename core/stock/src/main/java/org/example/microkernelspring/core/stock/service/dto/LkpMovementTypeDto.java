package org.example.microkernelspring.core.stock.service.dto;

import java.util.UUID;

public record LkpMovementTypeDto(
        UUID id,
        String code,
        String name,
        short direction
) {}
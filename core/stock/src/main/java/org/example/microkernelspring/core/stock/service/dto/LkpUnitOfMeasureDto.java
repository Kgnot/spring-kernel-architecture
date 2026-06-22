package org.example.microkernelspring.core.stock.service.dto;

import java.util.UUID;

public record LkpUnitOfMeasureDto(
        UUID id,
        String code,
        String name
) {}
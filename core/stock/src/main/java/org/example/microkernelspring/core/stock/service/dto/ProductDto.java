package org.example.microkernelspring.core.stock.service.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductDto(
        UUID id,
        UUID tenantId,
        String sku,
        String name,
        String description,
        UUID categoryId,
        UUID unitOfMeasureId,
        BigDecimal costPrice,
        BigDecimal salePrice,
        boolean active,
        Instant createdAt
) {}
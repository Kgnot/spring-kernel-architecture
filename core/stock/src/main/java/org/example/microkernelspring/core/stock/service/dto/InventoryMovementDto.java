package org.example.microkernelspring.core.stock.service.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record InventoryMovementDto(
        UUID id,
        UUID tenantId,
        UUID inventoryId,
        UUID movementTypeId,
        BigDecimal quantity,
        UUID supplierId,
        UUID referenceId,
        String notes,
        UUID createdBy,
        Instant createdAt
) {}
package org.example.microkernelspring.core.stock.controller.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record InventoryMovementResponse(
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

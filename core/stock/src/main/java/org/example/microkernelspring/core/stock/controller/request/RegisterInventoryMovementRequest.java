package org.example.microkernelspring.core.stock.controller.request;

import java.math.BigDecimal;
import java.util.UUID;

public record RegisterInventoryMovementRequest(
        UUID inventoryId,
        UUID movementTypeId,
        BigDecimal quantity,
        UUID supplierId,
        UUID referenceId,
        String notes,
        UUID createdBy
) {}

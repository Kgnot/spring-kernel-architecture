package org.example.microkernelspring.core.stock.controller.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record InventoryResponse(
        UUID id,
        UUID tenantId,
        UUID productId,
        UUID siteId,
        BigDecimal quantityOnHand,
        BigDecimal quantityReserved,
        BigDecimal reorderPoint,
        Instant updatedAt
) {}

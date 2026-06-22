package org.example.microkernelspring.core.stock.controller.request;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateInventoryRequest(
        UUID tenantId,
        UUID productId,
        UUID siteId,
        BigDecimal quantityOnHand,
        BigDecimal quantityReserved,
        BigDecimal reorderPoint
) {}

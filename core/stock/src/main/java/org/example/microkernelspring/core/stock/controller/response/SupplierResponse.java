package org.example.microkernelspring.core.stock.controller.response;

import java.util.UUID;

public record SupplierResponse(
        UUID id,
        UUID tenantId,
        String name,
        String taxId,
        boolean active
) {}

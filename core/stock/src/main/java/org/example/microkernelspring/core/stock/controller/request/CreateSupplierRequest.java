package org.example.microkernelspring.core.stock.controller.request;

import java.util.UUID;

public record CreateSupplierRequest(
        UUID tenantId,
        String name,
        String taxId,
        boolean active
) {}

package org.example.microkernelspring.core.stock.service.dto;

import java.util.UUID;

public record SupplierDto(
        UUID id,
        UUID tenantId,
        String name,
        String taxId,
        boolean active
) {}
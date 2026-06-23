package org.example.microkernelspring.core.sale.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CustomerDtoApi(
        UUID id,
        UUID tenantId,
        UUID profileId,
        String companyName,
        String taxId,
        BigDecimal creditLimit) {
}
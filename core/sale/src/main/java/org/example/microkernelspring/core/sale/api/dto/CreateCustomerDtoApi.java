package org.example.microkernelspring.core.sale.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateCustomerDtoApi(
        UUID tenantId,
        UUID profileId,
        UUID customerTypeId,
        String companyName,
        String taxId,
        BigDecimal creditLimit
) {}
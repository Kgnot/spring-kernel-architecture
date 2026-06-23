package org.example.microkernelspring.core.sale.application.usecase.command;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateCustomerCommand(
        UUID tenantId,
        UUID profileId,
        UUID customerTypeId,
        String companyName,
        String taxId,
        BigDecimal creditLimit
) {}
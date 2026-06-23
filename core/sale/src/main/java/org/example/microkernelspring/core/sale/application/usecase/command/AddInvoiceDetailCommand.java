package org.example.microkernelspring.core.sale.application.usecase.command;

import java.math.BigDecimal;
import java.util.UUID;

public record AddInvoiceDetailCommand(
        UUID invoiceId,
        UUID productId,
        UUID serviceId,
        String description,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal discount,
        BigDecimal taxRate,
        BigDecimal lineTotal
) {}
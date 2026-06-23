package org.example.microkernelspring.core.sale.usecase.command;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateInvoiceCommand(
        UUID tenantId,
        UUID siteId,
        UUID customerId,
        UUID employeeId,
        String invoiceNumber,
        UUID statusId,
        BigDecimal subtotal,
        BigDecimal taxTotal,
        BigDecimal discountTotal,
        BigDecimal grandTotal,
        String currency,
        Instant issuedAt,
        Instant dueAt
) {}
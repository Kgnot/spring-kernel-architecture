package org.example.microkernelspring.core.sale.infrastructure.controllers.request;

import java.math.BigDecimal;
import java.util.UUID;

public record AddInvoiceDetailRequest(
        UUID productId,
        UUID serviceId,
        String description,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal discount,
        BigDecimal taxRate,
        BigDecimal lineTotal
) {}

package org.example.microkernelspring.core.sale.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record InvoiceDetailDtoApi(
        UUID id, UUID invoiceId, UUID productId, UUID serviceId, String description,
        BigDecimal quantity, BigDecimal unitPrice, BigDecimal discount, BigDecimal taxRate, BigDecimal lineTotal
) {}
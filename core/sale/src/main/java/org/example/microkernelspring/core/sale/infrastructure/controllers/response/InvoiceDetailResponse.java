package org.example.microkernelspring.core.sale.infrastructure.controllers.response;

import java.math.BigDecimal;
import java.util.UUID;

public record InvoiceDetailResponse(UUID id, UUID invoiceId, UUID productId, UUID serviceId, String description, BigDecimal quantity, BigDecimal unitPrice, BigDecimal discount, BigDecimal taxRate, BigDecimal lineTotal) {}
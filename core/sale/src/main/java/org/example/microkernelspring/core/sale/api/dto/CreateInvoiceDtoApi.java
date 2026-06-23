package org.example.microkernelspring.core.sale.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateInvoiceDtoApi(
        UUID tenantId, UUID siteId, UUID customerId, UUID employeeId,
        String invoiceNumber, UUID statusId, BigDecimal subtotal, BigDecimal taxTotal,
        BigDecimal discountTotal, BigDecimal grandTotal, String currency,
        Instant issuedAt, Instant dueAt
) {}

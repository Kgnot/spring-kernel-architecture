package org.example.microkernelspring.controller.request;

import java.math.BigDecimal;

public record CreateSaleRequest(
        Long tenantId,
        BigDecimal amount
) {
}
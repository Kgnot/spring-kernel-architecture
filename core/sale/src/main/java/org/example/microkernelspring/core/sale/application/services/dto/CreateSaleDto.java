package org.example.microkernelspring.core.sale.application.services.dto;

import java.math.BigDecimal;

public record CreateSaleDto(
        BigDecimal amount
) {
}

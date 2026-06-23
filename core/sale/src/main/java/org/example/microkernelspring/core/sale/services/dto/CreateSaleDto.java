package org.example.microkernelspring.core.sale.services.dto;

import java.math.BigDecimal;

public record CreateSaleDto(
        BigDecimal amount
) {
}

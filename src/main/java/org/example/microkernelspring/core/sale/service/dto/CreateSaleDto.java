package org.example.microkernelspring.core.sale.service.dto;

import java.math.BigDecimal;

public record CreateSaleDto(
        BigDecimal amount
) {
}

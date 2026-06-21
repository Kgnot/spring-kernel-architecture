package org.example.microkernelspring.core.sale.controller.request;

public record CreateSaleRequest(
        Long tenantId,
        String amount
) {
}

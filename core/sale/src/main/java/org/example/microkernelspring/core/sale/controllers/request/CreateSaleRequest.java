package org.example.microkernelspring.core.sale.controllers.request;

public record CreateSaleRequest(
        Long tenantId,
        String amount
) {
}

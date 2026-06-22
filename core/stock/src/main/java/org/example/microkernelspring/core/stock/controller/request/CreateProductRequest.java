package org.example.microkernelspring.core.stock.controller.request;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductRequest(
        String sku,
        String name,
        String description,
        UUID categoryId,
        UUID unitOfMeasureId,
        BigDecimal costPrice,
        BigDecimal salePrice,
        boolean active
) {}
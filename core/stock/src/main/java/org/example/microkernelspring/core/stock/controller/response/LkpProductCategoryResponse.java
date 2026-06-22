package org.example.microkernelspring.core.stock.controller.response;

import java.util.UUID;

public record LkpProductCategoryResponse(
        UUID id,
        String code,
        String name,
        UUID parentCategoryId
) {
}

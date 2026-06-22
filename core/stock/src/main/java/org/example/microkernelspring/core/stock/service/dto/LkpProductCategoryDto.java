package org.example.microkernelspring.core.stock.service.dto;

import java.util.UUID;

public record LkpProductCategoryDto(
        UUID id,
        String code,
        String name,
        UUID parentCategoryId
) {}
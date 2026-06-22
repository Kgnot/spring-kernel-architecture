package org.example.microkernelspring.core.stock.service.mapper;

import org.example.microkernelspring.core.stock.entity.LkpProductCategory;
import org.example.microkernelspring.core.stock.service.dto.LkpProductCategoryDto;

public class LkpProductCategoryMapper {
    public static LkpProductCategoryDto toDto(LkpProductCategory entity) {
        if (entity == null) return null;
        return new LkpProductCategoryDto(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getParentCategory() != null ? entity.getParentCategory().getId() : null
        );
    }
}
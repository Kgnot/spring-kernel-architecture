package org.example.microkernelspring.core.stock.controller.mapper;

import org.example.microkernelspring.core.stock.controller.response.LkpProductCategoryResponse;
import org.example.microkernelspring.core.stock.service.dto.LkpProductCategoryDto;

public class LkpProductCategoryWebMapper {

    public static LkpProductCategoryResponse toResponse(LkpProductCategoryDto dto) {
        if (dto == null) return null;
        return new LkpProductCategoryResponse(
                dto.id(),
                dto.code(),
                dto.name(),
                dto.parentCategoryId()
        );
    }
}

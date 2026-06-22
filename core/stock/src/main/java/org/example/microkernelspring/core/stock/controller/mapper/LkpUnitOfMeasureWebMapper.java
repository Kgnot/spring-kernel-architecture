package org.example.microkernelspring.core.stock.controller.mapper;


import org.example.microkernelspring.core.stock.controller.response.LkpUnitOfMeasureResponse;
import org.example.microkernelspring.core.stock.service.dto.LkpUnitOfMeasureDto;

public class LkpUnitOfMeasureWebMapper {

    public static LkpUnitOfMeasureResponse toResponse(LkpUnitOfMeasureDto dto) {
        if (dto == null) return null;
        return new LkpUnitOfMeasureResponse(
                dto.id(),
                dto.code(),
                dto.name()
        );
    }
}

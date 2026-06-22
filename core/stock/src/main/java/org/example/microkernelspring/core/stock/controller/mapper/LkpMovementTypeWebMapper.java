package org.example.microkernelspring.core.stock.controller.mapper;


import org.example.microkernelspring.core.stock.controller.response.LkpMovementTypeResponse;
import org.example.microkernelspring.core.stock.service.dto.LkpMovementTypeDto;

public class LkpMovementTypeWebMapper {

    public static LkpMovementTypeResponse toResponse(LkpMovementTypeDto dto) {
        if (dto == null) return null;
        return new LkpMovementTypeResponse(
                dto.id(),
                dto.code(),
                dto.name(),
                dto.direction()
        );
    }
}

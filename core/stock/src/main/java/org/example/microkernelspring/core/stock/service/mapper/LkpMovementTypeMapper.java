package org.example.microkernelspring.core.stock.service.mapper;

import org.example.microkernelspring.core.stock.entity.LkpMovementType;
import org.example.microkernelspring.core.stock.service.dto.LkpMovementTypeDto;

public class LkpMovementTypeMapper {
    public static LkpMovementTypeDto toDto(LkpMovementType entity) {
        if (entity == null) return null;
        return new LkpMovementTypeDto(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDirection()
        );
    }
}
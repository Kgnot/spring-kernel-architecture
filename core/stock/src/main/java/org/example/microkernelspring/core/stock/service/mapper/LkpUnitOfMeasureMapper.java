package org.example.microkernelspring.core.stock.service.mapper;

import org.example.microkernelspring.core.stock.entity.LkpUnitOfMeasure;
import org.example.microkernelspring.core.stock.service.dto.LkpUnitOfMeasureDto;

public class LkpUnitOfMeasureMapper {
    public static LkpUnitOfMeasureDto toDto(LkpUnitOfMeasure entity) {
        if (entity == null) return null;
        return new LkpUnitOfMeasureDto(
                entity.getId(),
                entity.getCode(),
                entity.getName()
        );
    }
}
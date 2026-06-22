package org.example.microkernelspring.core.stock.service.mapper;

import org.example.microkernelspring.core.stock.entity.Supplier;
import org.example.microkernelspring.core.stock.service.dto.SupplierDto;

public class SupplierMapper {
    public static SupplierDto toDto(Supplier entity) {
        if (entity == null) return null;
        return new SupplierDto(
                entity.getId(),
                entity.getTenantId(),
                entity.getName(),
                entity.getTaxId(),
                entity.isActive()
        );
    }

    public static Supplier toEntity(SupplierDto dto) {
        if (dto == null) return null;
        Supplier entity = new Supplier();
        entity.setId(dto.id());
        entity.setTenantId(dto.tenantId());
        entity.setName(dto.name());
        entity.setTaxId(dto.taxId());
        entity.setActive(dto.active());
        return entity;
    }
}
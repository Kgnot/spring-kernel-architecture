package org.example.microkernelspring.core.stock.service.mapper;

import org.example.microkernelspring.core.stock.entity.Inventory;
import org.example.microkernelspring.core.stock.entity.InventoryMovement;
import org.example.microkernelspring.core.stock.entity.LkpMovementType;
import org.example.microkernelspring.core.stock.entity.Supplier;
import org.example.microkernelspring.core.stock.service.dto.InventoryMovementDto;

public class InventoryMovementMapper {
    public static InventoryMovementDto toDto(InventoryMovement entity) {
        if (entity == null) return null;
        return new InventoryMovementDto(
                entity.getId(),
                entity.getTenantId(),
                entity.getInventory() != null ? entity.getInventory().getId() : null,
                entity.getMovementType() != null ? entity.getMovementType().getId() : null,
                entity.getQuantity(),
                entity.getSupplier() != null ? entity.getSupplier().getId() : null,
                entity.getReferenceId(),
                entity.getNotes(),
                entity.getCreatedBy(),
                entity.getCreatedAt()
        );
    }

    public static InventoryMovement toEntity(InventoryMovementDto dto, Inventory inventory, LkpMovementType movementType, Supplier supplier) {
        if (dto == null) return null;
        InventoryMovement entity = new InventoryMovement();
        entity.setId(dto.id());
        entity.setTenantId(dto.tenantId());
        entity.setInventory(inventory);
        entity.setMovementType(movementType);
        entity.setQuantity(dto.quantity());
        entity.setSupplier(supplier);
        entity.setReferenceId(dto.referenceId());
        entity.setNotes(dto.notes());
        entity.setCreatedBy(dto.createdBy());
        return entity;
    }
}
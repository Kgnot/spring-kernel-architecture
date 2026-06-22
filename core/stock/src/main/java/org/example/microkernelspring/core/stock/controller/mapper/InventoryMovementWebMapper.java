package org.example.microkernelspring.core.stock.controller.mapper;

import org.example.microkernelspring.core.stock.controller.request.RegisterInventoryMovementRequest;
import org.example.microkernelspring.core.stock.controller.response.InventoryMovementResponse;
import org.example.microkernelspring.core.stock.service.dto.InventoryMovementDto;

public class InventoryMovementWebMapper {

    public static InventoryMovementDto toServiceDto(RegisterInventoryMovementRequest request) {
        if (request == null) return null;
        return new InventoryMovementDto(
                null,
                request.tenantId(),
                request.inventoryId(),
                request.movementTypeId(),
                request.quantity(),
                request.supplierId(),
                request.referenceId(),
                request.notes(),
                request.createdBy(),
                null
        );
    }

    public static InventoryMovementResponse toResponse(InventoryMovementDto dto) {
        if (dto == null) return null;
        return new InventoryMovementResponse(
                dto.id(),
                dto.tenantId(),
                dto.inventoryId(),
                dto.movementTypeId(),
                dto.quantity(),
                dto.supplierId(),
                dto.referenceId(),
                dto.notes(),
                dto.createdBy(),
                dto.createdAt()
        );
    }
}

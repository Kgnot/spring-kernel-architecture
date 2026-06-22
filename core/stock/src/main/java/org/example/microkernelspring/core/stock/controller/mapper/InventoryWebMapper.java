package org.example.microkernelspring.core.stock.controller.mapper;

import org.example.microkernelspring.core.stock.controller.request.CreateInventoryRequest;
import org.example.microkernelspring.core.stock.controller.response.InventoryResponse;
import org.example.microkernelspring.core.stock.service.dto.InventoryDto;

import java.util.UUID;

public class InventoryWebMapper {

    public static InventoryDto toServiceDto(CreateInventoryRequest request, UUID tenantId) {
        if (request == null) return null;
        return new InventoryDto(
                null,
                tenantId,
                request.productId(),
                request.siteId(),
                request.quantityOnHand(),
                request.quantityReserved(),
                request.reorderPoint(),
                null
        );
    }

    public static InventoryResponse toResponse(InventoryDto dto) {
        if (dto == null) return null;
        return new InventoryResponse(
                dto.id(),
                dto.tenantId(),
                dto.productId(),
                dto.siteId(),
                dto.quantityOnHand(),
                dto.quantityReserved(),
                dto.reorderPoint(),
                dto.updatedAt()
        );
    }
}

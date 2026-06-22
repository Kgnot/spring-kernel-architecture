package org.example.microkernelspring.core.stock.controller.mapper;

import org.example.microkernelspring.core.stock.controller.request.CreateSupplierRequest;
import org.example.microkernelspring.core.stock.controller.response.SupplierResponse;
import org.example.microkernelspring.core.stock.service.dto.SupplierDto;

import java.util.UUID;

public class SupplierWebMapper {

    public static SupplierDto toServiceDto(CreateSupplierRequest request, UUID tenantId) {
        if (request == null) return null;
        return new SupplierDto(
                null,
                tenantId,
                request.name(),
                request.taxId(),
                request.active()
        );
    }

    public static SupplierResponse toResponse(SupplierDto dto) {
        if (dto == null) return null;
        return new SupplierResponse(
                dto.id(),
                dto.tenantId(),
                dto.name(),
                dto.taxId(),
                dto.active()
        );
    }
}
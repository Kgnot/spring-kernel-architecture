package org.example.microkernelspring.core.stock.controller.mapper;

import org.example.microkernelspring.core.stock.controller.request.CreateSupplierRequest;
import org.example.microkernelspring.core.stock.controller.response.SupplierResponse;
import org.example.microkernelspring.core.stock.service.dto.SupplierDto;

public class SupplierWebMapper {

    public static SupplierDto toServiceDto(CreateSupplierRequest request) {
        if (request == null) return null;
        return new SupplierDto(
                null,
                request.tenantId(),
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
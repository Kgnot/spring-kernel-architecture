package org.example.microkernelspring.core.stock.controller.mapper;

import org.example.microkernelspring.core.stock.controller.request.CreateProductRequest;
import org.example.microkernelspring.core.stock.controller.response.ProductResponse;
import org.example.microkernelspring.core.stock.service.dto.ProductDto;

import java.util.UUID;

public class ProductWebMapper {

    public static ProductDto toServiceDto(CreateProductRequest request, UUID tenantId) {
        if (request == null) return null;
        return new ProductDto(
                null,
                tenantId,
                request.sku(),
                request.name(),
                request.description(),
                request.categoryId(),
                request.unitOfMeasureId(),
                request.costPrice(),
                request.salePrice(),
                request.active(),
                null
        );
    }

    public static ProductDto toServiceDto(UUID productId, UUID tenantId, CreateProductRequest request) {
        if (request == null) return null;
        return new ProductDto(
                productId,
                tenantId,
                request.sku(),
                request.name(),
                request.description(),
                request.categoryId(),
                request.unitOfMeasureId(),
                request.costPrice(),
                request.salePrice(),
                request.active(),
                null
        );
    }

    public static ProductResponse toResponse(ProductDto dto) {
        if (dto == null) return null;
        return new ProductResponse(
                dto.id(),
                dto.tenantId(),
                dto.sku(),
                dto.name(),
                dto.description(),
                dto.categoryId(),
                dto.unitOfMeasureId(),
                dto.costPrice(),
                dto.salePrice(),
                dto.active(),
                dto.createdAt()
        );
    }
}
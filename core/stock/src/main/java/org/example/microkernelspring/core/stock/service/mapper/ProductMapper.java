package org.example.microkernelspring.core.stock.service.mapper;

import org.example.microkernelspring.core.stock.entity.LkpProductCategory;
import org.example.microkernelspring.core.stock.entity.LkpUnitOfMeasure;
import org.example.microkernelspring.core.stock.entity.Product;
import org.example.microkernelspring.core.stock.service.dto.ProductDto;

public class ProductMapper {
    public static ProductDto toDto(Product entity) {
        if (entity == null) return null;
        return new ProductDto(
                entity.getId(),
                entity.getTenantId(),
                entity.getSku(),
                entity.getName(),
                entity.getDescription(),
                entity.getCategory() != null ? entity.getCategory().getId() : null,
                entity.getUnitOfMeasure() != null ? entity.getUnitOfMeasure().getId() : null,
                entity.getCostPrice(),
                entity.getSalePrice(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }

    public static Product toEntity(ProductDto dto, LkpProductCategory category, LkpUnitOfMeasure unitOfMeasure) {
        if (dto == null) return null;
        Product entity = new Product();
        entity.setId(dto.id());
        entity.setTenantId(dto.tenantId());
        entity.setSku(dto.sku());
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setCategory(category);
        entity.setUnitOfMeasure(unitOfMeasure);
        entity.setCostPrice(dto.costPrice());
        entity.setSalePrice(dto.salePrice());
        entity.setActive(dto.active());
        return entity;
    }
}
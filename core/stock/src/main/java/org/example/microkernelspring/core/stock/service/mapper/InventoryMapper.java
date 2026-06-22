package org.example.microkernelspring.core.stock.service.mapper;

import org.example.microkernelspring.core.stock.entity.Inventory;
import org.example.microkernelspring.core.stock.entity.Product;
import org.example.microkernelspring.core.stock.service.dto.InventoryDto;

public class InventoryMapper {
    public static InventoryDto toDto(Inventory entity) {
        if (entity == null) return null;
        return new InventoryDto(
                entity.getId(),
                entity.getTenantId(),
                entity.getProduct() != null ? entity.getProduct().getId() : null,
                entity.getSiteId(),
                entity.getQuantityOnHand(),
                entity.getQuantityReserved(),
                entity.getReorderPoint(),
                entity.getUpdatedAt()
        );
    }

    public static Inventory toEntity(InventoryDto dto, Product product) {
        if (dto == null) return null;
        Inventory entity = new Inventory();
        entity.setId(dto.id());
        entity.setTenantId(dto.tenantId());
        entity.setProduct(product);
        entity.setSiteId(dto.siteId());
        entity.setQuantityOnHand(dto.quantityOnHand());
        entity.setQuantityReserved(dto.quantityReserved());
        entity.setReorderPoint(dto.reorderPoint());
        return entity;
    }
}
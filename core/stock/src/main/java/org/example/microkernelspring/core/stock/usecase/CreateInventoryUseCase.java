package org.example.microkernelspring.core.stock.usecase;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.entity.Inventory;
import org.example.microkernelspring.core.stock.entity.Product;
import org.example.microkernelspring.core.stock.repository.InventoryRepository;
import org.example.microkernelspring.core.stock.repository.ProductRepository;
import org.example.microkernelspring.core.stock.service.dto.InventoryDto;
import org.example.microkernelspring.core.stock.service.mapper.InventoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateInventoryUseCase {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Transactional
    public InventoryDto execute(InventoryDto dto) {
        Product product = productRepository
                .findByIdAndTenantId(dto.productId(), dto.tenantId())
                .orElseThrow(() -> new RuntimeException("Product not found or access denied"));

        Inventory entity = InventoryMapper.toEntity(dto, product);

        return InventoryMapper.toDto(inventoryRepository.save(entity));
    }
}
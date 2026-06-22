package org.example.microkernelspring.core.stock.service;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.repository.ProductRepository;
import org.example.microkernelspring.core.stock.service.dto.ProductDto;
import org.example.microkernelspring.core.stock.service.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    @Transactional(readOnly = true)
    public List<ProductDto> findAllByTenant(UUID tenantId) {
        return repository.findAllByTenantId(tenantId).stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductDto findByIdAndTenant(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(ProductMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Product not found or access denied"));
    }
}
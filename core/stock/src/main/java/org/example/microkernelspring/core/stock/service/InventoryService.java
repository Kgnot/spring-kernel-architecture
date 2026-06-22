package org.example.microkernelspring.core.stock.service;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.repository.InventoryRepository;
import org.example.microkernelspring.core.stock.service.dto.InventoryDto;
import org.example.microkernelspring.core.stock.service.mapper.InventoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository repository;

    @Transactional(readOnly = true)
    public List<InventoryDto> findAllByTenant(UUID tenantId) {
        return repository.findAllByTenantId(tenantId).stream()
                .map(InventoryMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public InventoryDto findByIdAndTenant(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(InventoryMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Inventory not found or access denied"));
    }
}
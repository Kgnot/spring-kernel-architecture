package org.example.microkernelspring.core.stock.service;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.entity.InventoryMovement;
import org.example.microkernelspring.core.stock.repository.InventoryMovementRepository;
import org.example.microkernelspring.core.stock.service.dto.InventoryMovementDto;
import org.example.microkernelspring.core.stock.service.mapper.InventoryMovementMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryMovementService {

    private final InventoryMovementRepository movementRepository;

    @Transactional(readOnly = true)
    public List<InventoryMovementDto> findAllByTenant(UUID tenantId) {
        return movementRepository.findAllByTenantId(tenantId).stream()
                .map(InventoryMovementMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public InventoryMovementDto findByIdAndTenant(UUID id, UUID tenantId) {
        return movementRepository.findByIdAndTenantId(id, tenantId)
                .map(InventoryMovementMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Movement not found or access denied"));
    }

}
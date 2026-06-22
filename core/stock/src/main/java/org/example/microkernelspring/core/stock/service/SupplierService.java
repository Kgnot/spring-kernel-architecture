package org.example.microkernelspring.core.stock.service;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.repository.SupplierRepository;
import org.example.microkernelspring.core.stock.service.dto.SupplierDto;
import org.example.microkernelspring.core.stock.service.mapper.SupplierMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository repository;

    @Transactional(readOnly = true)
    public List<SupplierDto> findAllByTenant(UUID tenantId) {
        return repository.findAllByTenantId(tenantId).stream()
                .map(SupplierMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SupplierDto findByIdAndTenant(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(SupplierMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Supplier not found or access denied"));
    }
}
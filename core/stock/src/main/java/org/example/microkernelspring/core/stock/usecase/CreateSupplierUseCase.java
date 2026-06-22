package org.example.microkernelspring.core.stock.usecase;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.entity.Supplier;
import org.example.microkernelspring.core.stock.repository.SupplierRepository;
import org.example.microkernelspring.core.stock.service.dto.SupplierDto;
import org.example.microkernelspring.core.stock.service.mapper.SupplierMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateSupplierUseCase {

    private final SupplierRepository supplierRepository;

    @Transactional
    public SupplierDto execute(SupplierDto dto) {
        Supplier entity = SupplierMapper.toEntity(dto);

        return SupplierMapper.toDto(supplierRepository.save(entity));
    }
}
package org.example.microkernelspring.core.stock.service;

import org.example.microkernelspring.core.stock.repository.LkpMovementTypeRepository;
import org.example.microkernelspring.core.stock.service.dto.LkpMovementTypeDto;
import org.example.microkernelspring.core.stock.service.mapper.LkpMovementTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LkpMovementTypeService {

    private final LkpMovementTypeRepository repository;

    public List<LkpMovementTypeDto> findAll() {
        return repository.findAll().stream()
                .map(LkpMovementTypeMapper::toDto)
                .toList();
    }

    public LkpMovementTypeDto findById(UUID id) {
        return repository.findById(id)
                .map(LkpMovementTypeMapper::toDto)
                .orElseThrow(() -> new RuntimeException("MovementType not found"));
    }
}
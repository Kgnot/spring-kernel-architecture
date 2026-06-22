package org.example.microkernelspring.core.stock.service;

import org.example.microkernelspring.core.stock.repository.LkpUnitOfMeasureRepository;
import org.example.microkernelspring.core.stock.service.dto.LkpUnitOfMeasureDto;
import org.example.microkernelspring.core.stock.service.mapper.LkpUnitOfMeasureMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LkpUnitOfMeasureService {

    private final LkpUnitOfMeasureRepository repository;

    public List<LkpUnitOfMeasureDto> findAll() {
        return repository.findAll().stream()
                .map(LkpUnitOfMeasureMapper::toDto)
                .toList();
    }

    public LkpUnitOfMeasureDto findById(UUID id) {
        return repository.findById(id)
                .map(LkpUnitOfMeasureMapper::toDto)
                .orElseThrow(() -> new RuntimeException("UnitOfMeasure not found"));
    }
}
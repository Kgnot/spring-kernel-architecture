package org.example.microkernelspring.core.stock.service;

import org.example.microkernelspring.core.stock.repository.LkpProductCategoryRepository;
import org.example.microkernelspring.core.stock.service.dto.LkpProductCategoryDto;
import org.example.microkernelspring.core.stock.service.mapper.LkpProductCategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LkpProductCategoryService {

    private final LkpProductCategoryRepository repository;

    public List<LkpProductCategoryDto> findAll() {
        return repository.findAll().stream()
                .map(LkpProductCategoryMapper::toDto)
                .toList();
    }

    public LkpProductCategoryDto findById(UUID id) {
        return repository.findById(id)
                .map(LkpProductCategoryMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
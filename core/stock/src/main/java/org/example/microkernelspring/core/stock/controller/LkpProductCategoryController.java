package org.example.microkernelspring.core.stock.controller;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.controller.mapper.LkpProductCategoryWebMapper;
import org.example.microkernelspring.core.stock.controller.response.LkpProductCategoryResponse;
import org.example.microkernelspring.core.stock.service.LkpProductCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lkp-product-categories")
@RequiredArgsConstructor
public class LkpProductCategoryController {

    private final LkpProductCategoryService lkpProductCategoryService;

    @GetMapping
    public ResponseEntity<List<LkpProductCategoryResponse>> findAll() {
        List<LkpProductCategoryResponse> response = lkpProductCategoryService.findAll().stream()
                .map(LkpProductCategoryWebMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LkpProductCategoryResponse> findById(
            @PathVariable UUID id
    ) {
        LkpProductCategoryResponse response = LkpProductCategoryWebMapper.toResponse(
                lkpProductCategoryService.findById(id)
        );

        return ResponseEntity.ok(response);
    }
}
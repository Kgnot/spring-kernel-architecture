package org.example.microkernelspring.core.hr.controller;

import org.example.microkernelspring.core.hr.entity.LkpLedgerEntryType;
import org.example.microkernelspring.core.hr.service.LedgerEntryTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/catalogs/ledger-entry-types")
public class LedgerEntryTypeController {

    private final LedgerEntryTypeService ledgerEntryTypeService;

    public LedgerEntryTypeController(
            LedgerEntryTypeService ledgerEntryTypeService
    ) {
        this.ledgerEntryTypeService = ledgerEntryTypeService;
    }

    @GetMapping
    public ResponseEntity<List<LkpLedgerEntryType>> findAll() {
        return ResponseEntity.ok(
                ledgerEntryTypeService.findAll()
        );
    }
}
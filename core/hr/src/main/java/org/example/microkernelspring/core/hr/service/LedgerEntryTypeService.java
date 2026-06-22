package org.example.microkernelspring.core.hr.service;

import org.example.microkernelspring.core.hr.entity.LkpLedgerEntryType;
import org.example.microkernelspring.core.hr.repository.LkpLedgerEntryTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LedgerEntryTypeService {

    private final LkpLedgerEntryTypeRepository repository;

    public LedgerEntryTypeService(
            LkpLedgerEntryTypeRepository repository
    ) {
        this.repository = repository;
    }

    public List<LkpLedgerEntryType> findAll() {
        return repository.findAll();
    }
}
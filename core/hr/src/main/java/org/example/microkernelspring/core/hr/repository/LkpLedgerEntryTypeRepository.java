package org.example.microkernelspring.core.hr.repository;

import org.example.microkernelspring.core.hr.entity.LkpLedgerEntryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LkpLedgerEntryTypeRepository extends JpaRepository<LkpLedgerEntryType, UUID> {
}

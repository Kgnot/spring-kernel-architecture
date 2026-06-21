package org.example.microkernelspring.core.hr.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Tipos de movimiento del libro de créditos/débitos a empleados
 * (loan, repayment).
 */
@Entity
@Table(
        name = "lkp_ledger_entry_type",
        schema = "hr",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
public class LkpLedgerEntryType {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** loan, repayment */
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public LkpLedgerEntryType() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

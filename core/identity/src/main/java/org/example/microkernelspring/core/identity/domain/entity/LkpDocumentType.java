package org.example.microkernelspring.core.identity.domain.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Tipos de documento de identidad (CC, CE, NIT, passport, RUT).
 */
@Entity
@Table(
        name = "lkp_document_type",
        schema = "identity",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
public class LkpDocumentType {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** CC, CE, NIT, passport, RUT */
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public LkpDocumentType() {
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

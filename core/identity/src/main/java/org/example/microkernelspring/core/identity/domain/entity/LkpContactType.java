package org.example.microkernelspring.core.identity.domain.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Tipos de contacto de una persona (email, phone, whatsapp).
 */
@Entity
@Table(
        name = "lkp_contact_type",
        schema = "identity",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
public class LkpContactType {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** email, phone, whatsapp */
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    public LkpContactType() {
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

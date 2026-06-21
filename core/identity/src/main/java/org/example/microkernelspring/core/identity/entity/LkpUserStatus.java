package org.example.microkernelspring.core.identity.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Estados de una cuenta de login (pending_verification, active,
 * suspended, disabled).
 */
@Entity
@Table(
        name = "lkp_user_status",
        schema = "identity",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
public class LkpUserStatus {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** pending_verification, active, suspended, disabled */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public LkpUserStatus() {
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

package org.example.microkernelspring.core.identity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

/**
 * Datos de la persona física. NO guarda email/teléfono directamente (ver
 * UserContact): una persona puede tener varios de cada uno. hr.Employee y
 * sale.Customer referencian un profile (de forma lógica) en lugar de
 * duplicar datos personales.
 */
@Entity
@Table(name = "profile", schema = "identity")
@Data
public class Profile {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** Referencia LÓGICA a tenant.tenants.id (sin FK real entre schemas). */
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    /**
     * Nullable: un profile (ej. un cliente) puede no tener cuenta de
     * acceso. FK real porque UsersLogin vive en este mismo schema
     * identity. Relación 1-a-1 (ref: - en el DBML).
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_login_id", unique = true)
    private UsersLogin userLogin;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id")
    private LkpDocumentType documentType;

    @Column(name = "document_number", length = 50)
    private String documentNumber;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Profile() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}

package org.example.microkernelspring.core.identity.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * Múltiples emails/teléfonos por persona. Resuelve el caso de negocio: el
 * usuario puede tener 2+ emails y 2+ teléfonos, y solo UNO está marcado
 * isLoginEmail como el vigente para iniciar sesión (ese valor debe
 * sincronizarse hacia UsersLogin.email al cambiar).
 */
@Entity
@Table(name = "user_contact", schema = "identity")
public class UserContact {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contact_type_id", nullable = false)
    private LkpContactType contactType;

    /** El email o número en sí. */
    @Column(name = "value", nullable = false, length = 255)
    private String value;

    /** Contacto principal de ese tipo, para mostrar en UI. */
    @Column(name = "is_primary", nullable = false)
    private boolean primary = false;

    /**
     * true si este es el email vigente para autenticación; debe
     * sincronizarse hacia UsersLogin.email al cambiar.
     */
    @Column(name = "is_login_email", nullable = false)
    private boolean loginEmail = false;

    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public UserContact() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public LkpContactType getContactType() {
        return contactType;
    }

    public void setContactType(LkpContactType contactType) {
        this.contactType = contactType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(boolean loginEmail) {
        this.loginEmail = loginEmail;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

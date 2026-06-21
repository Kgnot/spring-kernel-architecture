package org.example.microkernelspring.core.identity.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Qué roles tiene asignado cada cuenta. Un usuario puede tener varios
 * roles (ej. cajero + bodeguero). FK real porque UsersLogin y Role viven
 * en este mismo schema identity. Usa clave compuesta (userLoginId, roleId)
 * vía @EmbeddedId.
 */
@Entity
@Table(name = "user_role", schema = "identity")
public class UserRole {

    @EmbeddedId
    private UserRoleId id = new UserRoleId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userLoginId")
    @JoinColumn(name = "user_login_id", nullable = false)
    private UsersLogin userLogin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    public UserRole() {
    }

    public UserRole(UsersLogin userLogin, Role role) {
        this.userLogin = userLogin;
        this.role = role;
        this.id = new UserRoleId(userLogin.getId(), role.getId());
    }

    @PrePersist
    protected void onCreate() {
        if (this.assignedAt == null) {
            this.assignedAt = Instant.now();
        }
    }

    public UserRoleId getId() {
        return id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public UsersLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UsersLogin userLogin) {
        this.userLogin = userLogin;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }
}

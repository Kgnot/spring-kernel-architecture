package org.example.microkernelspring.core.identity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/** Clave compuesta de UserRole: (userLoginId, roleId). */
@Embeddable
public class UserRoleId implements Serializable {

    @Column(name = "user_login_id")
    private UUID userLoginId;

    @Column(name = "role_id")
    private UUID roleId;

    public UserRoleId() {
    }

    public UserRoleId(UUID userLoginId, UUID roleId) {
        this.userLoginId = userLoginId;
        this.roleId = roleId;
    }

    public UUID getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(UUID userLoginId) {
        this.userLoginId = userLoginId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleId)) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(userLoginId, that.userLoginId) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userLoginId, roleId);
    }
}

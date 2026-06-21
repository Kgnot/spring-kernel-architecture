package org.example.microkernelspring.core.identity.application.repository;

import org.example.microkernelspring.core.identity.domain.entity.UserRole;
import org.example.microkernelspring.core.identity.domain.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    List<UserRole> findByUserLogin_Id(UUID userLoginId);

    /** Nombres de rol planos, listos para el LoginResponse. */
    @Query("""
            SELECT ur.role.name FROM UserRole ur
            WHERE ur.userLogin.id = :userLoginId
            """)
    List<String> findRoleNamesByUserLoginId(@Param("userLoginId") UUID userLoginId);

    /** Códigos de permiso efectivos del usuario, vía sus roles. */
    @Query("""
            SELECT DISTINCT perm.code
            FROM UserRole ur
            JOIN RolePermission rp ON rp.role.id = ur.role.id
            JOIN Permission perm ON perm.id = rp.permission.id
            WHERE ur.userLogin.id = :userLoginId
            """)
    List<String> findPermissionCodesByUserLoginId(@Param("userLoginId") UUID userLoginId);
}

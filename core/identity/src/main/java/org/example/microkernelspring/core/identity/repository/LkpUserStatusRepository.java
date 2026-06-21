package org.example.microkernelspring.core.identity.repository;

import org.example.microkernelspring.core.identity.entity.LkpUserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LkpUserStatusRepository extends JpaRepository<LkpUserStatus, UUID> {

    Optional<LkpUserStatus> findByCode(String code);
}

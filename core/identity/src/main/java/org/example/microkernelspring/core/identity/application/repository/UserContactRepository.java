package org.example.microkernelspring.core.identity.application.repository;

import org.example.microkernelspring.core.identity.domain.entity.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserContactRepository extends JpaRepository<UserContact, UUID> {

    List<UserContact> findByProfile_Id(UUID profileId);
}

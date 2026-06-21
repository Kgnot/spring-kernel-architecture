package org.example.microkernelspring.core.identity.repository;

import org.example.microkernelspring.core.identity.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    Optional<Profile> findByUserLogin_Id(UUID userLoginId);
}

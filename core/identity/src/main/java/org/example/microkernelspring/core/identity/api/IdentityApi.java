package org.example.microkernelspring.core.identity.api;

import org.example.microkernelspring.core.identity.api.dto.CustomerProfileDetails;

import java.util.Optional;
import java.util.UUID;

public interface IdentityApi {

    Optional<CustomerProfileDetails> getProfileDetails(UUID profileId);
}

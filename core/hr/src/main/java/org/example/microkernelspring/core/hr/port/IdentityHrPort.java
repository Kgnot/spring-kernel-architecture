package org.example.microkernelspring.core.hr.port;

import org.example.microkernelspring.core.identity.api.dto.CustomerProfileDetails;

import java.util.Optional;
import java.util.UUID;

public interface IdentityHrPort {

    Optional<CustomerProfileDetails> getProfileDetails(UUID profileId);


}

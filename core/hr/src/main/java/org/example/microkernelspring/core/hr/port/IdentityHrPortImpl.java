package org.example.microkernelspring.core.hr.port;

import org.example.microkernelspring.core.identity.api.IdentityApi;
import org.example.microkernelspring.core.identity.api.dto.CustomerProfileDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class IdentityHrPortImpl implements IdentityHrPort {

    private final IdentityApi identityApi;

    public IdentityHrPortImpl(IdentityApi identityApi) {
        this.identityApi = identityApi;
    }

    @Override
    public Optional<CustomerProfileDetails> getProfileDetails(UUID profileId) {
        return identityApi.getProfileDetails(profileId);
    }
}

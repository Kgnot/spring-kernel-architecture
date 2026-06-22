package org.example.microkernelspring.core.identity.api;

import org.example.microkernelspring.core.identity.api.dto.CustomerProfileDetails;
import org.example.microkernelspring.core.identity.application.repository.ProfileRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class IdentityApiLocal implements IdentityApi {

    private final ProfileRepository profileRepository;

    public IdentityApiLocal(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<CustomerProfileDetails> getProfileDetails(UUID profileId) {
        return profileRepository.findById(profileId)
                .map(profile -> new CustomerProfileDetails(
                        profile.getId(),
                        profile.getAvatarUrl(),
                        profile.getFirstName(),
                        profile.getLastName(),
                        profile.getDocumentNumber(),
                        profile.getCreatedAt()
                ));
    }
}



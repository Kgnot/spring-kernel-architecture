package org.example.microkernelspring.core.identity.application.service;

import org.example.microkernelspring.core.identity.application.repository.*;
import org.example.microkernelspring.core.identity.application.service.error.EmailAlreadyRegisteredException;
import org.example.microkernelspring.core.identity.application.usecase.command.RegisterUserCommand;
import org.example.microkernelspring.core.identity.application.usecase.result.RegisterUserResult;
import org.example.microkernelspring.core.identity.domain.entity.*;
import org.example.microkernelspring.core.tenant.api.dto.TenantBySubdomainDtoApi;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private static final String DEFAULT_ROLE_NAME = "viewer";

    private final UsersLoginRepository usersLoginRepository;
    private final ProfileRepository profileRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserContactRepository userContactRepository;
    private final LkpUserStatusRepository userStatusRepository;
    private final LkpContactTypeRepository contactTypeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(
            UsersLoginRepository usersLoginRepository,
            ProfileRepository profileRepository,
            UserRoleRepository userRoleRepository,
            UserContactRepository userContactRepository,
            LkpUserStatusRepository userStatusRepository,
            LkpContactTypeRepository contactTypeRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usersLoginRepository = usersLoginRepository;
        this.profileRepository = profileRepository;
        this.userRoleRepository = userRoleRepository;
        this.userContactRepository = userContactRepository;
        this.userStatusRepository = userStatusRepository;
        this.contactTypeRepository = contactTypeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterUserResult register(
            TenantBySubdomainDtoApi tenant,
            RegisterUserCommand command
    ) {
        if (usersLoginRepository.existsByTenantIdAndEmail(
                tenant.id(),
                command.email()
        )) {
            throw new EmailAlreadyRegisteredException(
                    "Ya existe una cuenta con ese email en este tenant."
            );
        }

        LkpUserStatus activeStatus = userStatusRepository.findByCode("active")
                .orElseThrow(() -> new IllegalStateException(
                        "No existe identity.lkp_user_status con código 'active'."
                ));

        LkpContactType emailType = contactTypeRepository.findByCode("email")
                .orElseThrow(() -> new IllegalStateException(
                        "No existe identity.lkp_contact_type con código 'email'."
                ));

        Role defaultRole = roleRepository
                .findByTenantIdIsNullAndName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new IllegalStateException(
                        "No existe el rol global '" + DEFAULT_ROLE_NAME + "'."
                ));

        Profile profile = new Profile();
        profile.setTenantId(tenant.id());
        profile.setFirstName(command.firstName());
        profile.setLastName(command.lastName());
        profile = profileRepository.save(profile);

        UserContact contact = new UserContact();
        contact.setProfile(profile);
        contact.setContactType(emailType);
        contact.setValue(command.email());
        contact.setPrimary(true);
        contact.setLoginEmail(true);
        contact.setVerified(false);
        userContactRepository.save(contact);

        UsersLogin usersLogin = new UsersLogin();
        usersLogin.setTenantId(tenant.id());
        usersLogin.setEmail(command.email());
        usersLogin.setPasswordHash(passwordEncoder.encode(command.password()));
        usersLogin.setStatus(activeStatus);
        usersLogin = usersLoginRepository.save(usersLogin);

        profile.setUserLogin(usersLogin);
        profileRepository.save(profile);

        userRoleRepository.save(new UserRole(usersLogin, defaultRole));

        String fullName = (profile.getFirstName() + " "
                + profile.getLastName()).trim();

        return new RegisterUserResult(
                usersLogin.getId(),
                profile.getId(),
                tenant.id(),
                usersLogin.getEmail(),
                fullName
        );
    }
}
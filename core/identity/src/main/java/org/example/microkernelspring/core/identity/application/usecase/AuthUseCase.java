package org.example.microkernelspring.core.identity.application.usecase;

import org.example.microkernelspring.core.identity.application.ports.TenantIdentityPort;
import org.example.microkernelspring.core.identity.application.repository.ProfileRepository;
import org.example.microkernelspring.core.identity.application.repository.UserRoleRepository;
import org.example.microkernelspring.core.identity.application.repository.UsersLoginRepository;
import org.example.microkernelspring.core.identity.application.service.LoginAttemptService;
import org.example.microkernelspring.core.identity.application.service.UserRegistrationService;
import org.example.microkernelspring.core.identity.application.service.error.InvalidCredentialsException;
import org.example.microkernelspring.core.identity.application.usecase.command.LoginCommand;
import org.example.microkernelspring.core.identity.application.usecase.command.RegisterUserCommand;
import org.example.microkernelspring.core.identity.application.usecase.result.LoginResult;
import org.example.microkernelspring.core.identity.application.usecase.result.RegisterUserResult;
import org.example.microkernelspring.core.identity.domain.entity.Profile;
import org.example.microkernelspring.core.identity.domain.entity.UsersLogin;
import org.example.microkernelspring.core.identity.domain.events.UserLoginFailedEvent;
import org.example.microkernelspring.core.identity.domain.events.UserLoginSucceededEvent;
import org.example.microkernelspring.core.identity.domain.events.UserRegisteredEvent;
import org.example.microkernelspring.core.tenant.api.dto.TenantBySubdomainDtoApi;
import org.example.microkernelspring.shared.application.events.EventBus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthUseCase {

    private final TenantIdentityPort tenantPort;
    private final UsersLoginRepository usersLoginRepository;
    private final ProfileRepository profileRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final UserRegistrationService userRegistrationService;
    private final EventBus eventBus;

    public AuthUseCase(
            TenantIdentityPort tenantPort,
            UsersLoginRepository usersLoginRepository,
            ProfileRepository profileRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder,
            LoginAttemptService loginAttemptService,
            UserRegistrationService userRegistrationService,
            EventBus eventBus
    ) {
        this.tenantPort = tenantPort;
        this.usersLoginRepository = usersLoginRepository;
        this.profileRepository = profileRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.userRegistrationService = userRegistrationService;
        this.eventBus = eventBus;
    }

    @Transactional
    public LoginResult login(LoginCommand command) {
        TenantBySubdomainDtoApi tenant = tenantPort
                .findBySubdomain(command.subdomain())
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Credenciales inválidas."
                ));

        UsersLogin user = usersLoginRepository
                .findByTenantIdAndEmail(tenant.id(), command.email())
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Credenciales inválidas."
                ));

        loginAttemptService.assertAccountIsUsable(user);

        if (!passwordEncoder.matches(command.password(), user.getPasswordHash())) {
            short attempts = loginAttemptService.registerFailedAttempt(user);

            eventBus.publish(new UserLoginFailedEvent(
                    UUID.randomUUID(),
                    Instant.now(),
                    tenant.id(),
                    user.getId(),
                    attempts
            ));

            throw new InvalidCredentialsException("Credenciales inválidas.");
        }

        loginAttemptService.registerSuccessfulLogin(user);

        eventBus.publish(new UserLoginSucceededEvent(
                UUID.randomUUID(),
                Instant.now(),
                tenant.id(),
                user.getId(),
                user.getEmail()
        ));

        String fullName = profileRepository.findByUserLogin_Id(user.getId())
                .map(this::toFullName)
                .orElse(null);

        List<String> roles = userRoleRepository.findRoleNamesByUserLoginId(user.getId());

        List<String> activePluginCodes = tenantPort.getActivePluginCodes(tenant.id())
                .pluginCodes();

        return new LoginResult(
                user.getId(),
                tenant.id(),
                tenant.subdomain(),
                user.getEmail(),
                fullName,
                roles,
                activePluginCodes
        );
    }

    @Transactional
    public RegisterUserResult register(RegisterUserCommand command) {
        TenantBySubdomainDtoApi tenant = tenantPort.findBySubdomain(command.subdomain())
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Tenant no encontrado."
                ));

        RegisterUserResult result = userRegistrationService.register(
                tenant,
                command
        );

        eventBus.publish(new UserRegisteredEvent(
                UUID.randomUUID(),
                Instant.now(),
                result.tenantId(),
                result.userLoginId(),
                result.profileId(),
                result.email(),
                result.fullName()
        ));

        return result;
    }

    private String toFullName(Profile profile) {
        return (profile.getFirstName() + " " + profile.getLastName()).trim();
    }
}
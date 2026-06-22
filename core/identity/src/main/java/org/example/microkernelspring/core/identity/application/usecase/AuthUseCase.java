package org.example.microkernelspring.core.identity.application.usecase;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.identity.application.usecase.result.RefreshResult;
import org.example.microkernelspring.shared.application.events.EventBus;
import org.example.microkernelspring.core.identity.application.ports.TenantIdentityPort;
import org.example.microkernelspring.core.identity.application.provider.JwtProvider;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthUseCase {

    private final TenantIdentityPort tenantPort;
    private final UsersLoginRepository usersLoginRepository;
    private final ProfileRepository profileRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final UserRegistrationService userRegistrationService;
    private final EventBus eventBus;
    private final JwtProvider jwtProvider;


    @Transactional
    public LoginResult login(LoginCommand command) {
        TenantBySubdomainDtoApi tenant = tenantPort.findBySubdomain(command.subdomain())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas."));

        UsersLogin user = usersLoginRepository.findByTenantIdAndEmail(tenant.id(), command.email())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas."));

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

        List<String> activePluginCodes = tenantPort.getActivePluginCodes(tenant.id()).pluginCodes();

        String accessToken = jwtProvider.generateAccessToken(user.getId(), tenant.id(), user.getEmail(), roles);
        String refreshToken = jwtProvider.generateRefreshToken(user.getId(), tenant.id());

        return new LoginResult(
                accessToken,
                refreshToken,
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

    @Transactional
    public RefreshResult refresh(String refreshToken) {
        Claims claims = jwtProvider.parseToken(refreshToken);

        if (claims == null) {
            throw new InvalidCredentialsException("Refresh token inválido o expirado.");
        }

        // Validar que el token sea de tipo REFRESH
        String type = claims.get("type", String.class);
        if (!"REFRESH".equals(type)) {
            throw new InvalidCredentialsException("Token inválido para refresco.");
        }

        UUID userId = UUID.fromString(claims.getSubject());
        UUID tenantId = UUID.fromString(claims.get("tenantId", String.class));

        // Opcional: Podrías buscar al usuario en DB para asegurarte que siga activo
        UsersLogin user = usersLoginRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("Usuario no encontrado."));

        // Volvemos a sacar los roles por si cambiaron desde el último login
        List<String> roles = userRoleRepository.findRoleNamesByUserLoginId(user.getId());

        // Generar nuevo Access Token (y opcionalmente rotar el Refresh Token)
        String newAccessToken = jwtProvider.generateAccessToken(user.getId(), tenantId, user.getEmail(), roles);
        String newRefreshToken = jwtProvider.generateRefreshToken(user.getId(), tenantId); // Rotación de refresh token

        return new RefreshResult(newAccessToken, newRefreshToken);
    }

    private String toFullName(Profile profile) {
        return (profile.getFirstName() + " " + profile.getLastName()).trim();
    }
}
package org.example.microkernelspring.core.identity.service;

import org.example.microkernelspring.core.identity.controller.dto.LoginRequest;
import org.example.microkernelspring.core.identity.controller.dto.LoginResponse;
import org.example.microkernelspring.core.identity.controller.dto.RegisterRequest;
import org.example.microkernelspring.core.identity.controller.dto.RegisterResponse;
import org.example.microkernelspring.core.identity.entity.*;
import org.example.microkernelspring.core.identity.repository.*;
import org.example.microkernelspring.core.tenant.persistence.entity.Tenant;
import org.example.microkernelspring.core.tenant.persistence.repository.TenantPluginRepository;
import org.example.microkernelspring.core.tenant.persistence.repository.TenantRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

// TODO - Aqui no se puede usar el repositorio debe usar un api del otro apartado
@Service
public class AuthService {

    /** Intentos fallidos permitidos antes de bloquear temporalmente la cuenta. */
    private static final short MAX_FAILED_ATTEMPTS = 5;

    /** Rol global asignado por defecto a toda cuenta nueva creada vía registro público. */
    private static final String DEFAULT_ROLE_NAME = "viewer";

    private final TenantRepository tenantRepository;
    private final UsersLoginRepository usersLoginRepository;
    private final ProfileRepository profileRepository;
    private final UserRoleRepository userRoleRepository;
    private final TenantPluginRepository tenantPluginRepository;
    private final UserContactRepository userContactRepository;
    private final LkpUserStatusRepository lkpUserStatusRepository;
    private final LkpContactTypeRepository lkpContactTypeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            TenantRepository tenantRepository,
            UsersLoginRepository usersLoginRepository,
            ProfileRepository profileRepository,
            UserRoleRepository userRoleRepository,
            TenantPluginRepository tenantPluginRepository,
            UserContactRepository userContactRepository,
            LkpUserStatusRepository lkpUserStatusRepository,
            LkpContactTypeRepository lkpContactTypeRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.tenantRepository = tenantRepository;
        this.usersLoginRepository = usersLoginRepository;
        this.profileRepository = profileRepository;
        this.userRoleRepository = userRoleRepository;
        this.tenantPluginRepository = tenantPluginRepository;
        this.userContactRepository = userContactRepository;
        this.lkpUserStatusRepository = lkpUserStatusRepository;
        this.lkpContactTypeRepository = lkpContactTypeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Tenant tenant = tenantRepository.findBySubdomain(request.subdomain())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas."));

        UsersLogin usersLogin = usersLoginRepository.findByTenantIdAndEmail(tenant.getId(), request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas."));

        assertAccountIsUsable(usersLogin);

        if (!passwordEncoder.matches(request.password(), usersLogin.getPasswordHash())) {
            registerFailedAttempt(usersLogin);
            throw new InvalidCredentialsException("Credenciales inválidas.");
        }

        registerSuccessfulLogin(usersLogin);

        String fullName = profileRepository.findByUserLogin_Id(usersLogin.getId())
                .map(this::toFullName)
                .orElse(null);

        List<String> roles = userRoleRepository.findRoleNamesByUserLoginId(usersLogin.getId());
        List<String> activePluginCodes = tenantPluginRepository.findActivePluginCodesByTenantId(tenant.getId());

        return new LoginResponse(
                usersLogin.getId(),
                tenant.getId(),
                tenant.getSubdomain(),
                usersLogin.getEmail(),
                fullName,
                roles,
                activePluginCodes
        );
    }

    /**
     * Crea una cuenta nueva dentro de un tenant existente: Profile +
     * UserContact (email marcado como login_email) + UsersLogin (password
     * hasheado con BCrypt) + UserRole (rol global por defecto "viewer").
     * Todo en una sola transacción: si algo falla, no debe quedar a medio
     * crear ni un Profile huérfano sin login.
     */
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        Tenant tenant = tenantRepository.findBySubdomain(request.subdomain())
                .orElseThrow(() -> new InvalidCredentialsException("Tenant no encontrado."));

        if (usersLoginRepository.existsByTenantIdAndEmail(tenant.getId(), request.email())) {
            throw new EmailAlreadyRegisteredException(
                    "Ya existe una cuenta con ese email en este tenant.");
        }

        LkpUserStatus activeStatus = lkpUserStatusRepository.findByCode("active")
                .orElseThrow(() -> new IllegalStateException(
                        "Catálogo identity.lkp_user_status no tiene el código 'active'. Corre 01_init.sql."));

        LkpContactType emailType = lkpContactTypeRepository.findByCode("email")
                .orElseThrow(() -> new IllegalStateException(
                        "Catálogo identity.lkp_contact_type no tiene el código 'email'. Corre 01_init.sql."));

        Role defaultRole = roleRepository.findByTenantIdIsNullAndName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new IllegalStateException(
                        "Rol global '" + DEFAULT_ROLE_NAME + "' no existe. Corre 01_init.sql."));

        // 1) Profile: datos de la persona física.
        Profile profile = new Profile();
        profile.setTenantId(tenant.getId());
        profile.setFirstName(request.firstName());
        profile.setLastName(request.lastName());
        profile = profileRepository.save(profile);

        // 2) UserContact: el email entra como contacto principal y vigente para login.
        UserContact contact = new UserContact();
        contact.setProfile(profile);
        contact.setContactType(emailType);
        contact.setValue(request.email());
        contact.setPrimary(true);
        contact.setLoginEmail(true);
        contact.setVerified(false);
        userContactRepository.save(contact);

        // 3) UsersLogin: la cuenta de acceso, con el password ya hasheado.
        UsersLogin usersLogin = new UsersLogin();
        usersLogin.setTenantId(tenant.getId());
        usersLogin.setEmail(request.email());
        usersLogin.setPasswordHash(passwordEncoder.encode(request.password()));
        usersLogin.setStatus(activeStatus);
        usersLogin = usersLoginRepository.save(usersLogin);

        // 4) Vincular Profile <-> UsersLogin (relación 1-a-1).
        profile.setUserLogin(usersLogin);
        profileRepository.save(profile);

        // 5) Rol por defecto.
        userRoleRepository.save(new UserRole(usersLogin, defaultRole));

        return new RegisterResponse(
                usersLogin.getId(),
                profile.getId(),
                tenant.getId(),
                usersLogin.getEmail(),
                (profile.getFirstName() + " " + profile.getLastName()).trim()
        );
    }

    private void assertAccountIsUsable(UsersLogin usersLogin) {
        if (usersLogin.getLockedUntil() != null && usersLogin.getLockedUntil().isAfter(Instant.now())) {
            throw new AccountNotAllowedException("La cuenta está temporalmente bloqueada. Intenta más tarde.");
        }

        String statusCode = usersLogin.getStatus().getCode();
        if (!"active".equals(statusCode)) {
            throw new AccountNotAllowedException(switch (statusCode) {
                case "pending_verification" -> "Debes verificar tu cuenta antes de iniciar sesión.";
                case "suspended" -> "Tu cuenta está suspendida. Contacta al administrador.";
                case "disabled" -> "Tu cuenta está deshabilitada.";
                default -> "No es posible iniciar sesión con esta cuenta.";
            });
        }
    }

    private void registerFailedAttempt(UsersLogin usersLogin) {
        short attempts = (short) (usersLogin.getFailedLoginAttempts() + 1);
        usersLogin.setFailedLoginAttempts(attempts);
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            usersLogin.setLockedUntil(Instant.now().plusSeconds(15 * 60));
        }
        usersLoginRepository.save(usersLogin);
    }

    private void registerSuccessfulLogin(UsersLogin usersLogin) {
        usersLogin.setFailedLoginAttempts((short) 0);
        usersLogin.setLockedUntil(null);
        usersLogin.setLastLoginAt(Instant.now());
        usersLoginRepository.save(usersLogin);
    }

    private String toFullName(Profile profile) {
        return (profile.getFirstName() + " " + profile.getLastName()).trim();
    }
}

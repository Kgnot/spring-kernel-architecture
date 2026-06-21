package org.example.microkernelspring.core.identity.application.service;

import org.example.microkernelspring.core.identity.application.repository.UsersLoginRepository;
import org.example.microkernelspring.core.identity.application.service.error.AccountNotAllowedException;
import org.example.microkernelspring.core.identity.domain.entity.UsersLogin;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LoginAttemptService {

    private static final short MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_DURATION_SECONDS = 15 * 60;

    private final UsersLoginRepository usersLoginRepository;

    public LoginAttemptService(UsersLoginRepository usersLoginRepository) {
        this.usersLoginRepository = usersLoginRepository;
    }

    public void assertAccountIsUsable(UsersLogin usersLogin) {
        if (usersLogin.getLockedUntil() != null
                && usersLogin.getLockedUntil().isAfter(Instant.now())) {
            throw new AccountNotAllowedException(
                    "La cuenta está temporalmente bloqueada. Intenta más tarde."
            );
        }

        String statusCode = usersLogin.getStatus().getCode();

        if (!"active".equals(statusCode)) {
            throw new AccountNotAllowedException(switch (statusCode) {
                case "pending_verification" ->
                        "Debes verificar tu cuenta antes de iniciar sesión.";
                case "suspended" ->
                        "Tu cuenta está suspendida. Contacta al administrador.";
                case "disabled" ->
                        "Tu cuenta está deshabilitada.";
                default ->
                        "No es posible iniciar sesión con esta cuenta.";
            });
        }
    }

    public short registerFailedAttempt(UsersLogin usersLogin) {
        short attempts = (short) (usersLogin.getFailedLoginAttempts() + 1);

        usersLogin.setFailedLoginAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            usersLogin.setLockedUntil(
                    Instant.now().plusSeconds(LOCK_DURATION_SECONDS)
            );
        }

        usersLoginRepository.save(usersLogin);
        return attempts;
    }

    public void registerSuccessfulLogin(UsersLogin usersLogin) {
        usersLogin.setFailedLoginAttempts((short) 0);
        usersLogin.setLockedUntil(null);
        usersLogin.setLastLoginAt(Instant.now());

        usersLoginRepository.save(usersLogin);
    }
}
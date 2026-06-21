package org.example.microkernelspring.core.identity.application.service.error;

/**
 * La cuenta existe y el tenant es válido, pero el acceso está bloqueado:
 * cuenta suspendida/deshabilitada, pendiente de verificación, o bloqueada
 * temporalmente por demasiados intentos fallidos.
 */
public class AccountNotAllowedException extends RuntimeException {

    public AccountNotAllowedException(String message) {
        super(message);
    }
}

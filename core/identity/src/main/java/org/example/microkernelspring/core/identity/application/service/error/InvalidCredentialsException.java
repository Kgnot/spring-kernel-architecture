package org.example.microkernelspring.core.identity.application.service.error;

/**
 * Credenciales inválidas: tenant/email no existen, o el password no
 * coincide. Se usa el mismo mensaje genérico para ambos casos a
 * propósito (no se revela cuál de los dos falló), para no dar pistas a
 * un atacante sobre si el email existe o no en el tenant.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}

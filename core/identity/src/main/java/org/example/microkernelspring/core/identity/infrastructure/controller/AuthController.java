package org.example.microkernelspring.core.identity.infrastructure.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.microkernelspring.core.identity.application.usecase.AuthUseCase;
import org.example.microkernelspring.core.identity.application.usecase.command.LoginCommand;
import org.example.microkernelspring.core.identity.application.usecase.command.RegisterUserCommand;
import org.example.microkernelspring.core.identity.application.usecase.result.LoginResult;
import org.example.microkernelspring.core.identity.application.usecase.result.RefreshResult;
import org.example.microkernelspring.core.identity.application.usecase.result.RegisterUserResult;
import org.example.microkernelspring.core.identity.infrastructure.controller.dto.LoginRequest;
import org.example.microkernelspring.core.identity.infrastructure.controller.dto.RefreshRequest;
import org.example.microkernelspring.core.identity.infrastructure.controller.dto.RegisterRequest;
import org.example.microkernelspring.core.identity.application.service.error.AccountNotAllowedException;
import org.example.microkernelspring.core.identity.application.service.error.EmailAlreadyRegisteredException;
import org.example.microkernelspring.core.identity.application.service.error.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class AuthController {

    private final AuthUseCase authService;

    public AuthController(AuthUseCase authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResult response = authService.login(new LoginCommand(request.subdomain(), request.email(), request.password()));
            log.info("LOGIN OK tenant={} email={}", response.tenantSubdomain(), response.email());
            return ResponseEntity.ok(response);

        } catch (InvalidCredentialsException ex) {
            log.warn("LOGIN FAILED subdomain={} email={} reason={}", request.subdomain(), request.email(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(ex.getMessage()));

        } catch (AccountNotAllowedException ex) {
            log.warn("LOGIN BLOCKED subdomain={} email={} reason={}", request.subdomain(), request.email(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(ex.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            RegisterUserResult response = authService.register(
                    new RegisterUserCommand(request.subdomain(), request.email(), request.password(),
                            request.firstName(), request.lastName())
            );
            log.info("REGISTER OK subdomain={} email={}", request.subdomain(), response.email());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (InvalidCredentialsException ex) {
            // Aquí significa "tenant no encontrado" (ver AuthService#register).
            log.warn("REGISTER FAILED subdomain={} reason={}", request.subdomain(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ex.getMessage()));

        } catch (EmailAlreadyRegisteredException ex) {
            log.warn("REGISTER CONFLICT subdomain={} email={} reason={}",
                    request.subdomain(), request.email(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(ex.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        try {
            RefreshResult response = authService.refresh(request.refreshToken());
            return ResponseEntity.ok(response);

        } catch (InvalidCredentialsException ex) {
            log.warn("REFRESH FAILED reason={}", ex.getMessage());
            // Si el refresh token falla, el frontend debe forzar logout
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(ex.getMessage()));
        }
    }

    record ErrorResponse(String message) {
    }
}

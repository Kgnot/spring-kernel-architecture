package org.example.microkernelspring.core.identity.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.microkernelspring.core.identity.controller.dto.LoginRequest;
import org.example.microkernelspring.core.identity.controller.dto.LoginResponse;
import org.example.microkernelspring.core.identity.controller.dto.RegisterRequest;
import org.example.microkernelspring.core.identity.controller.dto.RegisterResponse;
import org.example.microkernelspring.core.identity.service.AccountNotAllowedException;
import org.example.microkernelspring.core.identity.service.AuthService;
import org.example.microkernelspring.core.identity.service.EmailAlreadyRegisteredException;
import org.example.microkernelspring.core.identity.service.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            log.info("LOGIN OK tenant={} email={}", response.tenantSubdomain(), response.email());
            return ResponseEntity.ok(response);

        } catch (InvalidCredentialsException ex) {
            log.warn("LOGIN FAILED subdomain={} email={} reason={}",
                    request.subdomain(), request.email(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(ex.getMessage()));

        } catch (AccountNotAllowedException ex) {
            log.warn("LOGIN BLOCKED subdomain={} email={} reason={}",
                    request.subdomain(), request.email(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(ex.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = authService.register(request);
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

    record ErrorResponse(String message) {
    }
}

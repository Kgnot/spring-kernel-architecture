package org.example.microkernelspring.core.identity.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.microkernelspring.core.tenant.persistence.repository.TenantPluginRepository;
import org.example.microkernelspring.core.tenant.persistence.repository.TenantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class AuthController {

    private final TenantRepository tenantRepository;
    private final TenantPluginRepository tenantPluginRepository;

    public AuthController(TenantRepository tenantRepository, TenantPluginRepository tenantPluginRepository) {
        this.tenantRepository = tenantRepository;
        this.tenantPluginRepository = tenantPluginRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        return tenantRepository.findByEmail(request.email())
                .<ResponseEntity<?>>map(tenant -> {

                    List<String> plugins = tenantPluginRepository.findByTenant_IdAndEnabledTrue(tenant.getId())
                            .stream()
                            .map(tp -> tp.getPlugin().getId())
                            .toList();

                    log.info("LOGIN {} -> {}", tenant.getEmail(), plugins);

                    return ResponseEntity.ok(new LoginResponse(
                            tenant.getId(),
                            tenant.getSlug(),
                            tenant.getEmail(),
                            plugins
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(401).build()); // O el orElse que tengas
    }

    record LoginRequest(String email) {
    }

    record LoginResponse(Long id, String slug, String email, List<String> plugins) {
    }
}
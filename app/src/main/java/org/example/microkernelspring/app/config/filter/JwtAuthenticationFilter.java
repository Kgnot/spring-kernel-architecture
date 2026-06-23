package org.example.microkernelspring.app.config.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.microkernelspring.core.identity.application.provider.JwtProvider;
import org.example.microkernelspring.shared.infra.security.AuthenticatedUser;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.replace("Bearer ", "");
            Claims claims = jwtProvider.parseToken(token);
            // si el token es válido ahora miramos que sea de tipo ACCESS:
            if (claims != null && "ACCESS".equals(claims.get("type", String.class))) {
                // datos principales
                UUID userId = UUID.fromString(claims.getSubject());
                UUID tenantId = UUID.fromString(claims.get("tenantId", String.class));
                String email = claims.get("email", String.class);
                // Roles
                @SuppressWarnings("unchecked")
                List<String> roles = claims.get("roles", List.class);
                //Plugins
                @SuppressWarnings("unchecked")
                List<String> plugins = claims.get("plugins", List.class);
                // Ahora creamos el objeto de usuari autenticado
                AuthenticatedUser authenticatedUser = new AuthenticatedUser(userId, tenantId, email, roles, plugins);
                //convertimos roles a Authorities
                var authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();
                // Ponemos ahora el usuario en el contexto de Spring Security
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(authenticatedUser, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}

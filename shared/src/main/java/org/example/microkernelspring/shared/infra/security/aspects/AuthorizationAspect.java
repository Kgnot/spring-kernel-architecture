package org.example.microkernelspring.shared.infra.security.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.microkernelspring.shared.infra.security.AuthenticatedUser;
import org.example.microkernelspring.shared.infra.security.annotations.RequirePlugin;
import org.example.microkernelspring.shared.infra.security.annotations.RequireRoles;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class AuthorizationAspect {

    // Vamos a hacer que intercepte a cualquier method anotado con RequireRoles
    @Around("@annotation(org.example.microkernelspring.shared.infra.security.annotations.RequireRoles)")
    public Object checkRoles(ProceedingJoinPoint joinPoint) throws Throwable {
        AuthenticatedUser user = getAuthenticatedUser();

        RequireRoles annotation = getRequireRolesAnnotation(joinPoint);
        List<String> requiredRoles = Arrays.asList(annotation.value());

        // verificamos si el usuario tiene al menos uno de esos roles
        boolean hasRole = user.roles().stream().anyMatch(requiredRoles::contains);
        if (!hasRole) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes los roles necesarios para esta acción.");
        }

        return joinPoint.proceed();
    }

    // Para requerir los plugins
    @Around("@annotation(org.example.microkernelspring.shared.infra.security.annotations.RequirePlugin)")
    public Object checkPlugin(ProceedingJoinPoint joinPoint) throws Throwable {
        AuthenticatedUser user = getAuthenticatedUser();

        RequirePlugin annotation = getRequirePluginAnnotation(joinPoint);
        String requiredPlugin = annotation.value();

        // Verifica si el tenant del usuario tiene el plugin activo
        if (user.plugins() == null || !user.plugins().contains(requiredPlugin)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tu tenant no tiene acceso al módulo: " + requiredPlugin);
        }

        return joinPoint.proceed();
    }


    private AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser) {
            return (AuthenticatedUser) authentication.getPrincipal();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado.");
    }

    // helpers
    private RequireRoles getRequireRolesAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Class<?>[] parameterTypes = Arrays.stream(joinPoint.getArgs())
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
        return targetClass.getMethod(methodName, parameterTypes).getAnnotation(RequireRoles.class);
    }

    private RequirePlugin getRequirePluginAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Class<?>[] parameterTypes = Arrays.stream(joinPoint.getArgs())
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
        return targetClass.getMethod(methodName, parameterTypes).getAnnotation(RequirePlugin.class);
    }
}

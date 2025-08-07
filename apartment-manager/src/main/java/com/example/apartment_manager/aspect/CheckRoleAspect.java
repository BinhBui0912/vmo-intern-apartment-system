package com.example.apartment_manager.aspect;

import com.example.apartment_manager.annotation.CheckRole;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class CheckRoleAspect {
    @Before("@annotation(com.example.apartment_manager.annotation.CheckRole)")
    public void checkUserRole(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckRole checkRole = method.getAnnotation(CheckRole.class);
        String[] requiredRoles = checkRole.value();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }
        Set<String> roles = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        boolean hasRole = Arrays.stream(requiredRoles)
                .map(role -> "ROLE_" + role)
                .anyMatch(roles::contains);
        if (!hasRole) {
            throw new AccessDeniedException("Access denied: insufficient role");
        }
    }
}

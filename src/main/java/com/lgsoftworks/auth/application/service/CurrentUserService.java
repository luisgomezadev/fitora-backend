package com.lgsoftworks.auth.application.service;

import com.lgsoftworks.auth.domain.model.AuthenticatedUser;
import com.lgsoftworks.user.domain.enums.UserRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrentUserService {

    public AuthenticatedUser getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            throw new AccessDeniedException(
                    "Autenticación requerida"
            );
        }

        if (!(authentication.getPrincipal()
                instanceof AuthenticatedUser user)) {

            throw new AccessDeniedException(
                    "Usuario autenticado no válido"
            );
        }

        return user;
    }

    public UUID getUserId() {
        return getCurrentUser().getUserId();
    }

    public UUID getGymId() {
        return getCurrentUser().getGymId();
    }

    public String getEmail() {
        return getCurrentUser().getEmail();
    }

    public UserRole getRole() {
        return getCurrentUser().getRole();
    }
}
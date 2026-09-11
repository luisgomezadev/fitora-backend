package com.lgsoftworks.auth.domain.model;

import com.lgsoftworks.user.domain.enums.UserRole;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
public class AuthenticatedUser implements UserDetails {

    private final UUID userId;
    private final UUID gymId;
    private final String email;
    private final String password;
    private final UserRole role;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthenticatedUser(
            UUID userId,
            UUID gymId,
            String email,
            String password,
            UserRole role,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.userId = userId;
        this.gymId = gymId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
    }

    @Override
    public @NonNull String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
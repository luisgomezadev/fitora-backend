package com.lgsoftworks.auth.application.service;

import com.lgsoftworks.auth.domain.model.AuthenticatedUser;
import com.lgsoftworks.user.domain.exception.UserByEmailNotFoundException;
import com.lgsoftworks.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.lgsoftworks.user.infrastructure.adapter.out.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) {

        UserEntity user = userRepository
                .findByEmail(username)
                .orElseThrow(() ->
                        new UserByEmailNotFoundException(username)
                );

        return new AuthenticatedUser(
                user.getId(),
                user.getGymId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                List.of(
                        new SimpleGrantedAuthority(
                                user.getRole().name()
                        )
                )
        );
    }
}
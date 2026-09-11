package com.lgsoftworks.auth.application.service;

import com.lgsoftworks.auth.application.dto.request.LoginRequest;
import com.lgsoftworks.auth.application.dto.request.RefreshTokenRequest;
import com.lgsoftworks.auth.application.dto.response.AuthenticationResponse;
import com.lgsoftworks.auth.domain.exception.InvalidCredentialsException;
import com.lgsoftworks.auth.domain.model.AuthenticatedUser;
import com.lgsoftworks.emailverification.domain.exception.EmailNotVerifiedException;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.user.application.dto.mapper.UserModelMapper;
import com.lgsoftworks.user.domain.enums.UserRole;
import com.lgsoftworks.user.domain.exception.UserByEmailNotFoundException;
import com.lgsoftworks.user.domain.model.User;
import com.lgsoftworks.user.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepositoryPort userRepositoryPort;
    private final UserModelMapper userModelMapper;
    private final CustomUserDetailsService customUserDetailsService;

    public ApiResponse<AuthenticationResponse> authenticate(LoginRequest request) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(),
                                    request.getPassword()
                            )
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException();
        }

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) Objects.requireNonNull(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                ).getPrincipal();

        User user = userRepositoryPort
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserByEmailNotFoundException(request.getEmail())
                );

        // Validar correo únicamente para ADMIN
        if (user.getRole() == UserRole.ADMIN && !user.isEmailVerified()) {
            throw new EmailNotVerifiedException();
        }

        // El JWT se genera solamente después de pasar la validación
        assert authenticatedUser != null;
        String token = jwtService.generateToken(authenticatedUser);

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .token(token)
                .user(userModelMapper.toResponse(user))
                .build();

        return ApiResponse.<AuthenticationResponse>builder()
                .message("Autenticación exitosa")
                .data(authenticationResponse)
                .build();
    }

    public ApiResponse<AuthenticationResponse> refreshToken(RefreshTokenRequest request) {

        String email = jwtService.extractUsername(request.getToken());

        AuthenticatedUser user = (AuthenticatedUser) customUserDetailsService.loadUserByUsername(email);

        String token = jwtService.renewToken(request.getToken(), user);

        User domainUser = userRepositoryPort
                .findByEmail(email)
                .orElseThrow(() -> new UserByEmailNotFoundException(email));

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .token(token)
                .user(userModelMapper.toResponse(domainUser))
                .build();

        return ApiResponse.<AuthenticationResponse>builder()
                .message("Token renovado")
                .data(authenticationResponse)
                .build();
    }
}
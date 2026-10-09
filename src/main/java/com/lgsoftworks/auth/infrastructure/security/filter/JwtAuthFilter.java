package com.lgsoftworks.auth.infrastructure.security.filter;

import com.lgsoftworks.auth.application.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String jwt = authHeader.substring(7);

            String userEmail = jwtService.extractUsername(jwt);

            if (userEmail == null
                    || SecurityContextHolder
                    .getContext()
                    .getAuthentication() != null) {

                filterChain.doFilter(request, response);
                return;
            }

            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(userEmail);

            boolean expired =
                    jwtService.isTokenExpired(jwt);

            if (expired) {

                if (jwtService.canTokenBeRenewed(jwt)) {

                    SecurityContextHolder.clearContext();

                    filterChain.doFilter(request, response);
                    return;
                }

                String newToken =
                        jwtService.renewToken(
                                jwt,
                                userDetails
                        );

                response.setHeader(
                        "Authorization",
                        "Bearer " + newToken
                );

            } else {

                if (!jwtService.isTokenValid(
                        jwt,
                        userDetails
                )) {

                    SecurityContextHolder.clearContext();

                    filterChain.doFilter(request, response);
                    return;
                }
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);

        } catch (Exception e) {

            handlerExceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    e
            );

            return;
        }

        filterChain.doFilter(request, response);
    }
}
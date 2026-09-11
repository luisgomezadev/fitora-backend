package com.lgsoftworks.auth.infrastructure.adapter.in.web;

import com.lgsoftworks.auth.application.dto.request.LoginRequest;
import com.lgsoftworks.auth.application.dto.request.RefreshTokenRequest;
import com.lgsoftworks.auth.application.dto.request.RegisterRequest;
import com.lgsoftworks.auth.application.dto.response.AuthenticationResponse;
import com.lgsoftworks.auth.application.dto.response.RegistrationResponse;
import com.lgsoftworks.auth.application.service.AuthenticationService;
import com.lgsoftworks.auth.application.service.RegistrationService;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegistrationResponse>> registerGym(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @RequestBody @Valid LoginRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
            @RequestBody @Valid RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

}
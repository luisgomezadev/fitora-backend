package com.lgsoftworks.emailverification.infrastructure.adapter.in.web;

import com.lgsoftworks.emailverification.application.port.in.SendEmailVerificationUseCase;
import com.lgsoftworks.emailverification.application.port.in.VerifyEmailUseCase;
import com.lgsoftworks.emailverification.infrastructure.adapter.in.dto.SendEmailRequest;
import com.lgsoftworks.emailverification.infrastructure.adapter.in.dto.VerifyEmailRequest;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/emailverification")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final VerifyEmailUseCase verifyEmailUseCase;
    private final SendEmailVerificationUseCase sendEmailVerificationUseCase;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendEmail(
            @Valid @RequestBody SendEmailRequest request
    ) {

        sendEmailVerificationUseCase.execute(request.userId(), request.email());

        ApiResponse<String> response = ApiResponse.<String>builder()
                .message("Correo de verificación enviado")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verifyEmail(
            @Valid @RequestBody VerifyEmailRequest request
    ) {

        verifyEmailUseCase.verify(request.userId(), request.code());

        ApiResponse<String> response = ApiResponse.<String>builder()
                .message("Correo verificado correctamente")
                .build();

        return ResponseEntity.ok(response);
    }
}
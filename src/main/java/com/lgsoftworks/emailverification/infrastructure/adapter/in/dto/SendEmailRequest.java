package com.lgsoftworks.emailverification.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SendEmailRequest(
        @NotNull
        UUID userId,

        @NotBlank
        String email
) {

}

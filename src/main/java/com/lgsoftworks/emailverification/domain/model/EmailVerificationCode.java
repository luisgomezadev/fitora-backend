package com.lgsoftworks.emailverification.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class EmailVerificationCode {

    private final UUID id;
    private final UUID userId;
    private final String code;
    private final LocalDateTime expiresAt;
    private final LocalDateTime createdAt;

    private boolean used;

    public EmailVerificationCode(
            UUID id,
            UUID userId,
            String code,
            LocalDateTime expiresAt,
            LocalDateTime createdAt
    ) {
        this(
                id,
                userId,
                code,
                expiresAt,
                false,
                createdAt
        );
    }

    public EmailVerificationCode(
            UUID id,
            UUID userId,
            String code,
            LocalDateTime expiresAt,
            boolean used,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.code = code;
        this.expiresAt = expiresAt;
        this.used = used;
        this.createdAt = createdAt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean matches(String code) {
        return this.code.equals(code);
    }

    public void markAsUsed() {
        this.used = true;
    }
}
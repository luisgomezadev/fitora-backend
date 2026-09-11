package com.lgsoftworks.emailverification.domain.port.out;

import com.lgsoftworks.emailverification.domain.model.EmailVerificationCode;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationCodeRepositoryPort {
    void save(EmailVerificationCode verificationCode);
    Optional<EmailVerificationCode> findActiveByUserId(UUID userId);
}

package com.lgsoftworks.emailverification.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationCodeJpaRepository
        extends JpaRepository<EmailVerificationCodeEntity, UUID> {

    Optional<EmailVerificationCodeEntity>
    findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(UUID userId);

}
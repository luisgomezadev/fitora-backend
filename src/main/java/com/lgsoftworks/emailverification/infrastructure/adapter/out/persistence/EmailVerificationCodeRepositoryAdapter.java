package com.lgsoftworks.emailverification.infrastructure.adapter.out.persistence;

import com.lgsoftworks.emailverification.domain.model.EmailVerificationCode;
import com.lgsoftworks.emailverification.domain.port.out.EmailVerificationCodeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmailVerificationCodeRepositoryAdapter
        implements EmailVerificationCodeRepositoryPort {

    private final EmailVerificationCodeJpaRepository repository;

    @Override
    public void save(EmailVerificationCode code) {

        EmailVerificationCodeEntity entity =
                EmailVerificationCodeEntity.builder()
                        .id(code.getId())
                        .userId(code.getUserId())
                        .code(code.getCode())
                        .expiresAt(code.getExpiresAt())
                        .used(code.isUsed())
                        .createdAt(code.getCreatedAt())
                        .build();

        repository.save(entity);
    }

    @Override
    public Optional<EmailVerificationCode> findActiveByUserId(
            UUID userId
    ) {

        return repository
                .findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(userId)
                .map(entity ->
                        new EmailVerificationCode(
                                entity.getId(),
                                entity.getUserId(),
                                entity.getCode(),
                                entity.getExpiresAt(),
                                entity.isUsed(),
                                entity.getCreatedAt()
                        )
                );
    }
}
package com.lgsoftworks.emailverification.application.service;

import com.lgsoftworks.emailverification.application.port.in.SendEmailVerificationUseCase;
import com.lgsoftworks.emailverification.application.port.in.VerifyEmailUseCase;
import com.lgsoftworks.emailverification.domain.exception.EmailAlreadyVerifiedException;
import com.lgsoftworks.emailverification.domain.exception.InvalidVerificationCode;
import com.lgsoftworks.emailverification.domain.exception.VerificationCodeExpired;
import com.lgsoftworks.emailverification.domain.exception.VerificationCodeNotFound;
import com.lgsoftworks.emailverification.domain.model.EmailVerificationCode;
import com.lgsoftworks.emailverification.domain.port.out.EmailSender;
import com.lgsoftworks.emailverification.domain.port.out.EmailVerificationCodeRepositoryPort;
import com.lgsoftworks.user.domain.exception.UserByIdNotFoundException;
import com.lgsoftworks.user.domain.model.User;
import com.lgsoftworks.user.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService
        implements SendEmailVerificationUseCase, VerifyEmailUseCase {

    private final EmailVerificationCodeRepositoryPort codeRepository;
    private final EmailSender emailSender;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void execute(UUID userId, String email) {

        String code = generateCode();

        User user = userRepositoryPort
                .findById(userId)
                .orElseThrow(() -> new UserByIdNotFoundException(userId));

        if (user.isEmailVerified()) throw new EmailAlreadyVerifiedException();

        EmailVerificationCode verificationCode =
                new EmailVerificationCode(
                        UUID.randomUUID(),
                        userId,
                        code,
                        LocalDateTime.now().plusMinutes(10),
                        LocalDateTime.now()
                );

        codeRepository.save(verificationCode);

        emailSender.sendVerificationCode(email, code);
    }

    @Override
    @Transactional
    public void verify(UUID userId, String code) {

        EmailVerificationCode verificationCode = codeRepository
                .findActiveByUserId(userId)
                .orElseThrow(VerificationCodeNotFound::new);

        if (verificationCode.isExpired()) throw new VerificationCodeExpired();

        if (!verificationCode.matches(code)) throw new InvalidVerificationCode();

        User user = userRepositoryPort
                .findById(userId)
                .orElseThrow(() -> new UserByIdNotFoundException(userId));

        user.verifyEmail();

        verificationCode.markAsUsed();

        userRepositoryPort.save(user);
        codeRepository.save(verificationCode);
    }

    private String generateCode() {

        SecureRandom random = new SecureRandom();

        return String.format(
                "%06d",
                random.nextInt(1_000_000)
        );
    }
}
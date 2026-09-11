package com.lgsoftworks.emailverification.application.port.in;

import com.lgsoftworks.user.domain.model.User;

import java.util.UUID;

public interface SendEmailVerificationUseCase {
    void execute(UUID userId, String email);
}

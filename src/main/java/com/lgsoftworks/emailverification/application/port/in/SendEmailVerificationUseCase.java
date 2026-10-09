package com.lgsoftworks.emailverification.application.port.in;

import java.util.UUID;

public interface SendEmailVerificationUseCase {
    void execute(UUID userId, String email);
}

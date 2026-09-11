package com.lgsoftworks.emailverification.application.port.in;

import java.util.UUID;

public interface VerifyEmailUseCase {
    void verify(UUID userId, String code);
}

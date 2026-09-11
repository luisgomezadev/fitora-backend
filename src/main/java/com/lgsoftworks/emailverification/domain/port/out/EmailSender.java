package com.lgsoftworks.emailverification.domain.port.out;

public interface EmailSender {
    void sendVerificationCode(
            String email,
            String code
    );
}

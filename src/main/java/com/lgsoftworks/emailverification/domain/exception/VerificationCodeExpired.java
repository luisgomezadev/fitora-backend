package com.lgsoftworks.emailverification.domain.exception;

public class VerificationCodeExpired extends RuntimeException {
    public VerificationCodeExpired() {
        super("El código de verificación ha expirado");
    }
}

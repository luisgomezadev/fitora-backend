package com.lgsoftworks.emailverification.domain.exception;

public class VerificationCodeNotFound extends RuntimeException {
    public VerificationCodeNotFound() {
        super("Código de verificación no encontrado");
    }
}

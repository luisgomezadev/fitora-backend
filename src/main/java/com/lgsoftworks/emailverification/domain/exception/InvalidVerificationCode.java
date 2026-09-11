package com.lgsoftworks.emailverification.domain.exception;

public class InvalidVerificationCode extends RuntimeException {
    public InvalidVerificationCode() {
        super("El código de verificación es invalido");
    }
}

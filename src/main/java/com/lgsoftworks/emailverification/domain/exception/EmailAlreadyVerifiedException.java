package com.lgsoftworks.emailverification.domain.exception;

public class EmailAlreadyVerifiedException extends RuntimeException {
    public EmailAlreadyVerifiedException() {
        super("El correo ya está verificado");
    }
}

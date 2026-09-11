package com.lgsoftworks.emailverification.domain.exception;

public class EmailNotVerifiedException extends RuntimeException {
    public EmailNotVerifiedException() {
        super("El correo no está verificado");
    }
}

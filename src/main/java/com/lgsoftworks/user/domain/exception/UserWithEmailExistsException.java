package com.lgsoftworks.user.domain.exception;

public class UserWithEmailExistsException extends RuntimeException {
    public UserWithEmailExistsException(String email) {
        super("Un usuario con el email " + email + " ya está registrado en el gimnasio");
    }
}

package com.lgsoftworks.user.domain.exception;

public class UserByEmailNotFoundException extends RuntimeException {
    public UserByEmailNotFoundException(String email) {
        super("El usuario con email " + email + " no se encuentra registrado.");
    }
}

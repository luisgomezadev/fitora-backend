package com.lgsoftworks.user.domain.exception;

public class UserCreationNotAllowedException extends RuntimeException {
    public UserCreationNotAllowedException() {
        super("El usuario no tiene permisos para crear usuarios");
    }
}

package com.lgsoftworks.user.domain.exception;

public class UserWithUsernameExistsException extends RuntimeException {
    public UserWithUsernameExistsException(String username) {
        super("Un usuario con el username " + username + " ya está registrado en el gimnasio");
    }
}

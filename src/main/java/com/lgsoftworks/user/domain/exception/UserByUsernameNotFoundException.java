package com.lgsoftworks.user.domain.exception;

public class UserByUsernameNotFoundException extends RuntimeException {
    public UserByUsernameNotFoundException(String username) {
        super("El usuario con username " + username + " no se encuentra registrado.");
    }
}

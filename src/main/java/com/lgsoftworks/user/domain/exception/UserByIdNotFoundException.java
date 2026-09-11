package com.lgsoftworks.user.domain.exception;

import java.util.UUID;

public class UserByIdNotFoundException extends RuntimeException {
    public UserByIdNotFoundException(UUID id) {
        super("El usuario con ID " + id + " no se encuentra en el sistema");
    }
}

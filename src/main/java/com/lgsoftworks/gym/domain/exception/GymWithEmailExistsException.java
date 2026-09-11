package com.lgsoftworks.gym.domain.exception;

public class GymWithEmailExistsException extends RuntimeException {
    public GymWithEmailExistsException(String email) {
        super("Ya existe un gimnasio con el email: " + email);
    }
}
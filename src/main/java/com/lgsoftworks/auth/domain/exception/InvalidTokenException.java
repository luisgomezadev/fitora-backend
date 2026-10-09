package com.lgsoftworks.auth.domain.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
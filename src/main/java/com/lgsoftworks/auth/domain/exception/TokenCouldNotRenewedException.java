package com.lgsoftworks.auth.domain.exception;

public class TokenCouldNotRenewedException extends RuntimeException {
    public TokenCouldNotRenewedException(String message) {
        super(message);
    }
}

package com.lgsoftworks.user.domain.exception;

import com.lgsoftworks.shared.domain.exception.DomainException;

public class InvalidUserDataException extends DomainException {
    public InvalidUserDataException(String message) {
        super(message);
    }
}

package com.lgsoftworks.gym.domain.exception;

import com.lgsoftworks.shared.domain.exception.DomainException;

public class InvalidGymDataException extends DomainException {
    public InvalidGymDataException(String message) {
        super(message);
    }
}

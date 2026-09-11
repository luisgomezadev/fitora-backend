package com.lgsoftworks.membership.domain.exception;

import com.lgsoftworks.shared.domain.exception.DomainException;

public class InvalidMembershipDataException extends DomainException {
    public InvalidMembershipDataException(String message) {
        super(message);
    }
}

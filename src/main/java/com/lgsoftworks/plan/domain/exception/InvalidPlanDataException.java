package com.lgsoftworks.plan.domain.exception;

import com.lgsoftworks.shared.domain.exception.DomainException;

public class InvalidPlanDataException extends DomainException {
    public InvalidPlanDataException(String message) {
        super(message);
    }
}

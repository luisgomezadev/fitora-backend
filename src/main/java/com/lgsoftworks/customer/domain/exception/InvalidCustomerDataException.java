package com.lgsoftworks.customer.domain.exception;

import com.lgsoftworks.shared.domain.exception.DomainException;

public class InvalidCustomerDataException extends DomainException {
    public InvalidCustomerDataException(String message) {
        super(message);
    }
}

package com.lgsoftworks.membership.domain.exception;

public class CustomerIsNotActiveException extends RuntimeException {
    public CustomerIsNotActiveException() {
        super("El cliente no está activo");
    }
}

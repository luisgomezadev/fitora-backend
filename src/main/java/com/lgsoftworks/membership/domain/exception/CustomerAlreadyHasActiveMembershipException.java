package com.lgsoftworks.membership.domain.exception;

public class CustomerAlreadyHasActiveMembershipException extends RuntimeException {
    public CustomerAlreadyHasActiveMembershipException() {
        super("El cliente ya cuenta con una membresía activa");
    }
}
package com.lgsoftworks.attendance.domain.exception;

public class CustomerWithoutActiveMembershipException extends RuntimeException {
    public CustomerWithoutActiveMembershipException() {
        super("El cliente no tiene una membresía activa.");
    }
}

package com.lgsoftworks.membership.domain.exception;

public class MembershipByIdNotFoundException extends RuntimeException {
    public MembershipByIdNotFoundException() {
        super("Membresía con ID no encontrado");
    }
}

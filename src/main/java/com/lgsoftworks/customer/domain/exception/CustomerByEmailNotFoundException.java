package com.lgsoftworks.customer.domain.exception;

public class CustomerByEmailNotFoundException extends RuntimeException {
    public CustomerByEmailNotFoundException(String email) {
        super("El cliente con correo " + email + " no se encuentra en el gimnasio");
    }
}

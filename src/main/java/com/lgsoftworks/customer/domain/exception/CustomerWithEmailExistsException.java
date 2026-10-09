package com.lgsoftworks.customer.domain.exception;

public class CustomerWithEmailExistsException extends RuntimeException {
    public CustomerWithEmailExistsException(String email) {
        super("Un cliente con el correo " + email + " ya está registrado en el gimnasio");
    }
}

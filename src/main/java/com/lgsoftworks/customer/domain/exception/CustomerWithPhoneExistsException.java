package com.lgsoftworks.customer.domain.exception;

public class CustomerWithPhoneExistsException extends RuntimeException {
    public CustomerWithPhoneExistsException(String phone) {
        super("Un cliente con el número de celular " + phone + " ya está registrado en el gimnasio");
    }
}

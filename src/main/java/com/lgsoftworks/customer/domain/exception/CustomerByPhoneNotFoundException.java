package com.lgsoftworks.customer.domain.exception;

public class CustomerByPhoneNotFoundException extends RuntimeException {
    public CustomerByPhoneNotFoundException(String phone) {
        super("El cliente con número de celular " + phone + " no se encuentra en el gimnasio");
    }
}

package com.lgsoftworks.customer.domain.exception;

public class CustomerByDocumentNumberNotFoundException extends RuntimeException {
    public CustomerByDocumentNumberNotFoundException(String documentNumber) {
        super("El cliente con número de documento " + documentNumber + " no se encuentra en el gimnasio");
    }
}

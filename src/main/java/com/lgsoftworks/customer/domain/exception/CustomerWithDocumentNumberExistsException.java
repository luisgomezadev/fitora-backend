package com.lgsoftworks.customer.domain.exception;

public class CustomerWithDocumentNumberExistsException extends RuntimeException {
    public CustomerWithDocumentNumberExistsException(String documentNumber) {
        super("Un cliente con el número de documento " + documentNumber + " ya está registrado en el gimnasio");
    }
}

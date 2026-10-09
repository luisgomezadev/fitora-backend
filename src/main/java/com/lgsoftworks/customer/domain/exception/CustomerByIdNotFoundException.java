package com.lgsoftworks.customer.domain.exception;

import java.util.UUID;

public class CustomerByIdNotFoundException extends RuntimeException {
    public CustomerByIdNotFoundException(UUID id) {
        super("El cliente con ID " + id + " no se encuentra en el gimnasio");
    }
}

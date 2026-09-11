package com.lgsoftworks.student.domain.exception;

import java.util.UUID;

public class StudentByIdNotFoundException extends RuntimeException {
    public StudentByIdNotFoundException(UUID id) {
        super("El alumno con ID " + id + " no se encuentra en el gimnasio");
    }
}

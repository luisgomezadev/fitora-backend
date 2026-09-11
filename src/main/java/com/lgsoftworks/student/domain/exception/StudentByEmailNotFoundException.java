package com.lgsoftworks.student.domain.exception;

public class StudentByEmailNotFoundException extends RuntimeException {
    public StudentByEmailNotFoundException(String email) {
        super("El alumno con correo " + email + " no se encuentra en el gimnasio");
    }
}

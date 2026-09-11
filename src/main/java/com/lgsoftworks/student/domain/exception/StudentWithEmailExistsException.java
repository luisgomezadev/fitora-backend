package com.lgsoftworks.student.domain.exception;

public class StudentWithEmailExistsException extends RuntimeException {
    public StudentWithEmailExistsException(String email) {
        super("Un alumno con el correo " + email + " ya está registrado en el gimnasio");
    }
}

package com.lgsoftworks.student.domain.exception;

public class StudentWithDocumentNumberExistsException extends RuntimeException {
    public StudentWithDocumentNumberExistsException(String documentNumber) {
        super("Un alumno con el número de documento " + documentNumber + " ya está registrado en el gimnasio");
    }
}

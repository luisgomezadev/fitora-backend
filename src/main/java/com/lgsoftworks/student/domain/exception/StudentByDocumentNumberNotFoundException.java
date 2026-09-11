package com.lgsoftworks.student.domain.exception;

public class StudentByDocumentNumberNotFoundException extends RuntimeException {
    public StudentByDocumentNumberNotFoundException(String documentNumber) {
        super("El alumno con número de documento " + documentNumber + " no se encuentra en el gimnasio");
    }
}

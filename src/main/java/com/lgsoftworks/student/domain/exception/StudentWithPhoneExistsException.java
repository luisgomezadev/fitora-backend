package com.lgsoftworks.student.domain.exception;

public class StudentWithPhoneExistsException extends RuntimeException {
    public StudentWithPhoneExistsException(String phone) {
        super("Un alumno con el número de celular " + phone + " ya está registrado en el gimnasio");
    }
}

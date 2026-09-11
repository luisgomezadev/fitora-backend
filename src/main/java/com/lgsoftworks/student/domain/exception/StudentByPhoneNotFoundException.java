package com.lgsoftworks.student.domain.exception;

public class StudentByPhoneNotFoundException extends RuntimeException {
    public StudentByPhoneNotFoundException(String phone) {
        super("El alumno con número de celular " + phone + " no se encuentra en el gimnasio");
    }
}

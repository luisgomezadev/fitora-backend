package com.lgsoftworks.attendance.domain.exception;

public class OpenAttendanceNotFoundException extends RuntimeException {
    public OpenAttendanceNotFoundException() {
        super("El cliente no tiene una asistencia abierta");
    }
}
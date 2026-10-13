package com.lgsoftworks.attendance.domain.exception;

import java.util.UUID;

public class AttendanceByIdNotFoundException extends RuntimeException {
    public AttendanceByIdNotFoundException(UUID attendanceId) {
        super("Asistencia con ID " + attendanceId + " no encontrada.");
    }
}

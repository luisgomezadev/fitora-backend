package com.lgsoftworks.attendance.domain.exception;

import com.lgsoftworks.shared.domain.exception.DomainException;

public class InvalidAttendanceDataException extends DomainException {
    public InvalidAttendanceDataException(String message) {
        super(message);
    }
}

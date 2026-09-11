package com.lgsoftworks.student.domain.exception;

import com.lgsoftworks.shared.domain.exception.DomainException;

public class InvalidStudentDataException extends DomainException {
    public InvalidStudentDataException(String message) {
        super(message);
    }
}

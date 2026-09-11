package com.lgsoftworks.student.domain.service;

import com.lgsoftworks.student.domain.exception.StudentWithDocumentNumberExistsException;
import com.lgsoftworks.student.domain.exception.StudentWithEmailExistsException;
import com.lgsoftworks.student.domain.exception.StudentWithPhoneExistsException;
import com.lgsoftworks.student.domain.port.out.StudentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StudentUniquenessValidator {

    private final StudentRepositoryPort studentRepositoryPort;

    public void validate(
            String email,
            String documentNumber,
            String phone,
            UUID gymId
    ) {
        validateExistsDocumentNumber(documentNumber, gymId);
        validateExistsEmail(email, gymId);
        validateExistsPhone(phone, gymId);
    }

    private void validateExistsEmail(String email, UUID gymId) {

        if (email == null || email.isBlank()) {
            return;
        }

        boolean exists =
                studentRepositoryPort.existsByEmailAndGymId(email, gymId);

        if (exists) {
            throw new StudentWithEmailExistsException(email);
        }
    }

    private void validateExistsDocumentNumber(
            String documentNumber,
            UUID gymId
    ) {
        boolean exists =
                studentRepositoryPort.existsByDocumentNumberAndGymId(
                        documentNumber,
                        gymId
                );

        if (exists) {
            throw new StudentWithDocumentNumberExistsException(documentNumber);
        }
    }

    private void validateExistsPhone(String phone, UUID gymId) {

        boolean exists =
                studentRepositoryPort.existsByPhoneAndGymId(phone, gymId);

        if (exists) {
            throw new StudentWithPhoneExistsException(phone);
        }
    }
}

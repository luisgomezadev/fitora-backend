package com.lgsoftworks.customer.domain.service;

import com.lgsoftworks.customer.domain.exception.CustomerWithDocumentNumberExistsException;
import com.lgsoftworks.customer.domain.exception.CustomerWithEmailExistsException;
import com.lgsoftworks.customer.domain.exception.CustomerWithPhoneExistsException;
import com.lgsoftworks.customer.domain.port.out.CustomerRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerUniquenessValidator {

    private final CustomerRepositoryPort customerRepositoryPort;

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
                customerRepositoryPort.existsByEmailAndGymId(email, gymId);

        if (exists) {
            throw new CustomerWithEmailExistsException(email);
        }
    }

    private void validateExistsDocumentNumber(
            String documentNumber,
            UUID gymId
    ) {
        boolean exists =
                customerRepositoryPort.existsByDocumentNumberAndGymId(
                        documentNumber,
                        gymId
                );

        if (exists) {
            throw new CustomerWithDocumentNumberExistsException(documentNumber);
        }
    }

    private void validateExistsPhone(String phone, UUID gymId) {

        boolean exists =
                customerRepositoryPort.existsByPhoneAndGymId(phone, gymId);

        if (exists) {
            throw new CustomerWithPhoneExistsException(phone);
        }
    }
}

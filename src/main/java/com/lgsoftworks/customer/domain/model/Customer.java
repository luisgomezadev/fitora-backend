package com.lgsoftworks.customer.domain.model;

import com.lgsoftworks.customer.domain.enums.CustomerStatus;
import com.lgsoftworks.customer.domain.enums.DocumentType;
import com.lgsoftworks.customer.domain.exception.InvalidCustomerDataException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class Customer {

    private UUID id;
    private UUID gymId;

    private DocumentType documentType;
    private String documentNumber;

    private String firstName;
    private String lastName;

    private String email;
    private String phone;

    private LocalDate birthDate;

    private CustomerStatus status;

    private LocalDate registrationDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Customer(
            UUID gymId,
            DocumentType documentType,
            String documentNumber,
            String firstName,
            String lastName,
            String email,
            String phone,
            LocalDate birthDate
    ) {
        validateGymId(gymId);
        validateDocumentType(documentType);
        validateDocumentNumber(documentNumber);
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validateEmail(email);
        validatePhone(phone);
        validateBirthDate(birthDate);

        this.id = UUID.randomUUID();
        this.gymId = gymId;
        this.documentType = documentType;
        this.documentNumber = documentNumber.trim();
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email != null
                ? email.trim().toLowerCase()
                : null;
        this.phone = phone.trim();
        this.birthDate = birthDate;
        this.status = CustomerStatus.ACTIVE;
        this.registrationDate = LocalDate.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Customer create(
            UUID gymId,
            DocumentType documentType,
            String documentNumber,
            String firstName,
            String lastName,
            String email,
            String phone,
            LocalDate birthDate
    ) {
        return new Customer(
                gymId,
                documentType,
                documentNumber,
                firstName,
                lastName,
                email,
                phone,
                birthDate
        );
    }

    public static Customer reconstitute(
            UUID id,
            UUID gymId,
            DocumentType documentType,
            String documentNumber,
            String firstName,
            String lastName,
            String email,
            String phone,
            LocalDate birthDate,
            CustomerStatus status,
            LocalDate registrationDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        Customer customer = new Customer();

        customer.id = id;
        customer.gymId = gymId;
        customer.documentType = documentType;
        customer.documentNumber = documentNumber;
        customer.firstName = firstName;
        customer.lastName = lastName;
        customer.email = email;
        customer.phone = phone;
        customer.birthDate = birthDate;
        customer.status = status;
        customer.registrationDate = registrationDate;
        customer.createdAt = createdAt;
        customer.updatedAt = updatedAt;

        return customer;
    }

    private void validateGymId(UUID gymId) {
        if (gymId == null) {
            throw new InvalidCustomerDataException("Gym is required");
        }
    }

    private void validateDocumentType(DocumentType documentType) {
        if (documentType == null) {
            throw new InvalidCustomerDataException("Document type is required");
        }
    }

    private void validateDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new InvalidCustomerDataException("Document number is required");
        }

        if (documentNumber.trim().length() > 30) {
            throw new InvalidCustomerDataException(
                    "Document number cannot exceed 30 characters"
            );
        }
    }

    private void validateName(String name, String field) {
        if (name == null || name.isBlank()) {
            throw new InvalidCustomerDataException(field + " is required");
        }

        if (name.trim().length() < 2) {
            throw new InvalidCustomerDataException(
                    field + " must have at least 2 characters"
            );
        }

        if (name.trim().length() > 50) {
            throw new InvalidCustomerDataException(
                    field + " cannot exceed 50 characters"
            );
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            return;
        }

        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.trim().matches(regex)) {
            throw new InvalidCustomerDataException("Invalid email format");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new InvalidCustomerDataException("Phone is required");
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new InvalidCustomerDataException("Birth date is required");
        }

        if (birthDate.isAfter(LocalDate.now())) {
            throw new InvalidCustomerDataException(
                    "Birth date cannot be in the future"
            );
        }
    }

    public void updateInformation(
            String firstName,
            String lastName,
            String email,
            String phone,
            LocalDate birthDate
    ) {
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validateEmail(email);
        validatePhone(phone);
        validateBirthDate(birthDate);

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email != null
                ? email.trim().toLowerCase()
                : null;
        this.phone = phone.trim();
        this.birthDate = birthDate;

        touch();
    }

    public void activate() {
        this.status = CustomerStatus.ACTIVE;
        touch();
    }

    public void deactivate() {
        this.status = CustomerStatus.INACTIVE;
        touch();
    }

    public void suspend() {
        this.status = CustomerStatus.SUSPENDED;
        touch();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}
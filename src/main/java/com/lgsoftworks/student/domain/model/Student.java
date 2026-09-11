package com.lgsoftworks.student.domain.model;

import com.lgsoftworks.student.domain.enums.DocumentType;
import com.lgsoftworks.student.domain.enums.StudentStatus;
import com.lgsoftworks.student.domain.exception.InvalidStudentDataException;
import com.lgsoftworks.user.domain.enums.UserRole;
import com.lgsoftworks.user.domain.enums.UserStatus;
import com.lgsoftworks.user.domain.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class Student {

    private UUID id;
    private UUID gymId;

    private DocumentType documentType;
    private String documentNumber;

    private String firstName;
    private String lastName;

    private String email;
    private String phone;

    private LocalDate birthDate;

    private StudentStatus status;

    private LocalDate registrationDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Student(
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
        this.status = StudentStatus.ACTIVE;
        this.registrationDate = LocalDate.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Student create(
            UUID gymId,
            DocumentType documentType,
            String documentNumber,
            String firstName,
            String lastName,
            String email,
            String phone,
            LocalDate birthDate
    ) {
        return new Student(
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

    public static Student reconstitute(
            UUID id,
            UUID gymId,
            DocumentType documentType,
            String documentNumber,
            String firstName,
            String lastName,
            String email,
            String phone,
            LocalDate birthDate,
            StudentStatus status,
            LocalDate registrationDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        Student student = new Student();

        student.id = id;
        student.gymId = gymId;
        student.documentType = documentType;
        student.documentNumber = documentNumber;
        student.firstName = firstName;
        student.lastName = lastName;
        student.email = email;
        student.phone = phone;
        student.birthDate = birthDate;
        student.status = status;
        student.registrationDate = registrationDate;
        student.createdAt = createdAt;
        student.updatedAt = updatedAt;

        return student;
    }

    private void validateGymId(UUID gymId) {
        if (gymId == null) {
            throw new InvalidStudentDataException("Gym is required");
        }
    }

    private void validateDocumentType(DocumentType documentType) {
        if (documentType == null) {
            throw new InvalidStudentDataException("Document type is required");
        }
    }

    private void validateDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new InvalidStudentDataException("Document number is required");
        }

        if (documentNumber.trim().length() > 30) {
            throw new InvalidStudentDataException(
                    "Document number cannot exceed 30 characters"
            );
        }
    }

    private void validateName(String name, String field) {
        if (name == null || name.isBlank()) {
            throw new InvalidStudentDataException(field + " is required");
        }

        if (name.trim().length() < 2) {
            throw new InvalidStudentDataException(
                    field + " must have at least 2 characters"
            );
        }

        if (name.trim().length() > 50) {
            throw new InvalidStudentDataException(
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
            throw new InvalidStudentDataException("Invalid email format");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new InvalidStudentDataException("Phone is required");
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new InvalidStudentDataException("Birth date is required");
        }

        if (birthDate.isAfter(LocalDate.now())) {
            throw new InvalidStudentDataException(
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
        this.status = StudentStatus.ACTIVE;
        touch();
    }

    public void deactivate() {
        this.status = StudentStatus.INACTIVE;
        touch();
    }

    public void suspend() {
        this.status = StudentStatus.SUSPENDED;
        touch();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}
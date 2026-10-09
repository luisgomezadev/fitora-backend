package com.lgsoftworks.customer.infrastructure.adapter.out.persistence.entity;

import com.lgsoftworks.customer.domain.enums.CustomerStatus;
import com.lgsoftworks.customer.domain.enums.DocumentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "customers",
        indexes = {
                @Index(name = "idx_customer_gym_id", columnList = "gym_id"),
                @Index(name = "idx_customer_document", columnList = "document_type, document_number"),
                @Index(name = "idx_customer_email", columnList = "email"),
                @Index(name = "idx_customer_phone", columnList = "phone")}
)
@Getter
@Setter
@NoArgsConstructor
public class CustomerEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "gym_id", nullable = false)
    private UUID gymId;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 10)
    private DocumentType documentType;

    @Column(name = "document_number", nullable = false, length = 30)
    private String documentNumber;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CustomerStatus status;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
package com.lgsoftworks.customer.application.dto.response;

import com.lgsoftworks.customer.domain.enums.DocumentType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private UUID id;

    private DocumentType documentType;
    private String documentNumber;

    private String firstName;
    private String lastName;
    private String fullName;

    private String email;
    private String phone;

    private LocalDate birthDate;

    private LocalDate registrationDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
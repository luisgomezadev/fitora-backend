package com.lgsoftworks.student.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentUpdateRequest {
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 50,
            message = "El nombre debe tener entre 2 y 50 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es requerido")
    @Size(min = 2, max = 50,
            message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    @Email(message = "El email no es válido")
    @Size(max = 100,
            message = "El email no puede superar los 100 caracteres")
    private String email;

    @NotBlank(message = "El teléfono es requerido")
    @Size(min = 7, max = 10,
            message = "El teléfono debe tener entre 7 y 10 caracteres")
    private String phone;

    @NotNull(message = "La fecha de nacimiento es requerida")
    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    private LocalDate birthDate;
}

package com.lgsoftworks.auth.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull(message = "Correo obligatorio")
    @Email(message = "Correo invalido")
    private String email;
    @NotNull(message = "Contraseña obligatoria")
    @Length(min = 8, message = "Contraseña debe tener más de 8 caracteres")
    private String password;
}
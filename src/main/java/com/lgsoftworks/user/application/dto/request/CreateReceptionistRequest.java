package com.lgsoftworks.user.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReceptionistRequest {

    @NotBlank(message = "El username es requerido")
    @Size(
            min = 4,
            max = 50,
            message = "El username debe tener entre 4 y 50 caracteres"
    )
    private String username;

    @NotBlank(message = "El email es requerido")
    @Email(message = "Debe ingresar un correo válido")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Size(
            min = 8,
            message = "La contraseña debe tener al menos 8 caracteres"
    )
    private String password;
}

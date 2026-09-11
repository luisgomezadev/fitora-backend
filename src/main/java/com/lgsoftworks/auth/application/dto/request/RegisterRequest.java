package com.lgsoftworks.auth.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@Getter
@Setter
public class RegisterRequest {

    // Gym

    @NotBlank(message = "El nombre del gimnasio es requerido")
    @Size(min = 3, max = 100, message = "El nombre del gimnasio debe tener entre 1 y 100 caracteres")
    private String gymName;

    @NotBlank(message = "El documento del gimnasio es requerido")
    @Size(max = 50, message = "El documento del gimnasio no debe superar los 50 caracteres")
    private String gymDocument;

    @Email(message = "El email del gimnasio no es válido")
    @Size(
            max = 100,
            message = "El email del gimnasio no debe superar los 100 caracteres"
    )
    private String gymEmail;

    @Size(
            min = 7,
            max = 10,
            message = "El teléfono del gimnasio debe tener entre 7 y 10 caracteres"
    )
    private String gymPhone;

    @NotBlank(message = "La dirección del gimnasio es requerida")
    @Size(max = 100)
    private String gymAddress;

    @NotBlank(message = "La ciudad del gimnasio es requerida")
    @Size(max = 100)
    private String gymCity;


    // Admin

    @NotBlank(message = "El username es requerido")
    @Size(min = 4, max = 50, message = "El username debe tener entre 4 y 50 caracteres")
    @Pattern(
            regexp = "^[A-Za-z0-9._]+$",
            message = "El username solo puede contener letras, números, puntos y guiones bajos"
    )
    private String username;

    @NotBlank(message = "El email es requerido")
    @Email(message = "Debe ingresar un correo válido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Length(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

}

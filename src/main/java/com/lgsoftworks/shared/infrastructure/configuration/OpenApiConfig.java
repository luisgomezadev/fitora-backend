package com.lgsoftworks.shared.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Fitora API",
                version = "1.0.0",
                description = "Documentación de la API para el sistema de gestión de gimnasio.",
                contact = @Contact(
                        name = "Luis Gómez",
                        email = "luisgomez24g@gmail.com",
                        url = "https://luisgomezdev.vercel.app/"
                ),
                license = @License(
                        name = "MIT",
                        url = "https://opensource.org/licenses/MIT"
                )
        )
)
public class OpenApiConfig {

}
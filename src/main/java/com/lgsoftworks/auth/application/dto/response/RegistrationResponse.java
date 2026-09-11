package com.lgsoftworks.auth.application.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationResponse {

    private UUID gymId;
    private UUID userId;
    private String username;
    private String email;
    private String role;

}
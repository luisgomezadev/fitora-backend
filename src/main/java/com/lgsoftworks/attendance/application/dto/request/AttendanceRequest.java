package com.lgsoftworks.attendance.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AttendanceRequest {

    @NotNull(message = "El cliente es obligatorio")
    private UUID customerId;

}
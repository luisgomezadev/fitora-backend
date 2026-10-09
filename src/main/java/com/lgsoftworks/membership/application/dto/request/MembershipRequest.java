package com.lgsoftworks.membership.application.dto.request;

import com.lgsoftworks.membership.domain.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class MembershipRequest {

    @NotNull(message = "El cliente es requerido")
    private UUID customerId;

    @NotNull(message = "El plan es requerido")
    private UUID planId;

    @NotNull(message = "La fecha de inicio es requerida")
    private LocalDate startDate;

    @NotNull(message = "El precio de la membresía es requerido")
    @DecimalMin(value = "0.0", message = "El precio de la membresía no puede ser negativo")
    private BigDecimal price;

    @NotNull(message = "El método de pago es requerido")
    private PaymentMethod paymentMethod;

}
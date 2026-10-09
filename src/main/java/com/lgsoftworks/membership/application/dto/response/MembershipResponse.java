package com.lgsoftworks.membership.application.dto.response;

import com.lgsoftworks.membership.domain.enums.MembershipStatus;
import com.lgsoftworks.membership.domain.enums.PaymentMethod;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record MembershipResponse(
        UUID id,
        UUID customerId,
        String customerFullName,
        UUID planId,
        String planName,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal price,
        PaymentMethod paymentMethod,
        LocalDateTime paymentDate,
        MembershipStatus status
) {
}
package com.lgsoftworks.membership.domain.model;

import com.lgsoftworks.membership.domain.enums.MembershipStatus;
import com.lgsoftworks.membership.domain.enums.PaymentMethod;
import com.lgsoftworks.membership.domain.exception.InvalidMembershipDataException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class Membership {

    private UUID id;

    private UUID gymId;
    private UUID customerId;
    private UUID planId;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal price;

    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDate;

    private UUID registeredBy;

    private MembershipStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Membership(
            UUID gymId,
            UUID customerId,
            UUID planId,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal price,
            PaymentMethod paymentMethod,
            UUID registeredBy
    ) {
        validateIds(gymId, customerId, planId);
        validateDates(startDate, endDate);
        validatePrice(price);
        validatePayment(paymentMethod, registeredBy);

        this.id = UUID.randomUUID();
        this.gymId = gymId;
        this.customerId = customerId;
        this.planId = planId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();
        this.registeredBy = registeredBy;
        this.status = MembershipStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Membership create(
            UUID gymId,
            UUID customerId,
            UUID planId,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal price,
            PaymentMethod paymentMethod,
            UUID registeredBy
    ) {
        return new Membership(
                gymId,
                customerId,
                planId,
                startDate,
                endDate,
                price,
                paymentMethod,
                registeredBy
        );
    }

    public static Membership reconstitute(
            UUID id,
            UUID gymId,
            UUID customerId,
            UUID planId,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal price,
            PaymentMethod paymentMethod,
            LocalDateTime paymentDate,
            UUID registeredBy,
            MembershipStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        Membership membership = new Membership();

        membership.id = id;
        membership.gymId = gymId;
        membership.customerId = customerId;
        membership.planId = planId;
        membership.startDate = startDate;
        membership.endDate = endDate;
        membership.price = price;
        membership.paymentMethod = paymentMethod;
        membership.paymentDate = paymentDate;
        membership.registeredBy = registeredBy;
        membership.status = status;
        membership.createdAt = createdAt;
        membership.updatedAt = updatedAt;

        return membership;
    }

    private void validateIds(
            UUID gymId,
            UUID customerId,
            UUID planId
    ) {
        if (gymId == null) {
            throw new InvalidMembershipDataException("Gym is required");
        }

        if (customerId == null) {
            throw new InvalidMembershipDataException("Customer is required");
        }

        if (planId == null) {
            throw new InvalidMembershipDataException("Plan is required");
        }
    }

    private void validateDates(
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate == null) {
            throw new InvalidMembershipDataException(
                    "Start date is required"
            );
        }

        if (endDate == null) {
            throw new InvalidMembershipDataException(
                    "End date is required"
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new InvalidMembershipDataException(
                    "End date cannot be before start date"
            );
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new InvalidMembershipDataException(
                    "Membership price is required"
            );
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMembershipDataException(
                    "Membership price cannot be negative"
            );
        }
    }

    private void validatePayment(
            PaymentMethod paymentMethod,
            UUID registeredBy
    ) {
        if (paymentMethod == null) {
            throw new InvalidMembershipDataException(
                    "Payment method is required"
            );
        }

        if (registeredBy == null) {
            throw new InvalidMembershipDataException(
                    "User who registered the payment is required"
            );
        }
    }

    public boolean isValid(LocalDate date) {

        if (status != MembershipStatus.ACTIVE) {
            return false;
        }

        return !date.isBefore(startDate)
                && !date.isAfter(endDate);
    }

    public void cancel() {

        if (status == MembershipStatus.CANCELLED) {
            throw new InvalidMembershipDataException(
                    "Membership is already cancelled"
            );
        }

        this.status = MembershipStatus.CANCELLED;
        touch();
    }

    public void expire() {
        this.status = MembershipStatus.EXPIRED;
        touch();
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}
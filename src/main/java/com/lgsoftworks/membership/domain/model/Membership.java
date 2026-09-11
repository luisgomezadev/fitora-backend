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
    private UUID studentId;
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
            UUID studentId,
            UUID planId,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal price,
            PaymentMethod paymentMethod,
            LocalDateTime paymentDate,
            UUID registeredBy
    ) {
        validateIds(gymId, studentId, planId);
        validateDates(startDate, endDate);
        validatePrice(price);
        validatePayment(paymentMethod, paymentDate, registeredBy);

        this.id = UUID.randomUUID();
        this.gymId = gymId;
        this.studentId = studentId;
        this.planId = planId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.registeredBy = registeredBy;
        this.status = MembershipStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Membership create(
            UUID gymId,
            UUID studentId,
            UUID planId,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal price,
            PaymentMethod paymentMethod,
            LocalDateTime paymentDate,
            UUID registeredBy
    ) {
        return new Membership(
                gymId,
                studentId,
                planId,
                startDate,
                endDate,
                price,
                paymentMethod,
                paymentDate,
                registeredBy
        );
    }

    private void validateIds(
            UUID gymId,
            UUID studentId,
            UUID planId
    ) {
        if (gymId == null) {
            throw new InvalidMembershipDataException("Gym is required");
        }

        if (studentId == null) {
            throw new InvalidMembershipDataException("Student is required");
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
            LocalDateTime paymentDate,
            UUID registeredBy
    ) {
        if (paymentMethod == null) {
            throw new InvalidMembershipDataException(
                    "Payment method is required"
            );
        }

        if (paymentDate == null) {
            throw new InvalidMembershipDataException(
                    "Payment date is required"
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
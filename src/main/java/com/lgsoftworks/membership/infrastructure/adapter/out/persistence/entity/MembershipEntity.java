package com.lgsoftworks.membership.infrastructure.adapter.out.persistence.entity;

import com.lgsoftworks.membership.domain.enums.MembershipStatus;
import com.lgsoftworks.membership.domain.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "memberships",
        indexes = {
                @Index(name = "idx_membership_gym_id", columnList = "gym_id"),
                @Index(name = "idx_membership_customer_id", columnList = "customer_id"),
                @Index(name = "idx_membership_status", columnList = "status")}
)
@Getter
@Setter
@NoArgsConstructor
public class MembershipEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "gym_id", nullable = false)
    private UUID gymId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "plan_id", nullable = false)
    private UUID planId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "registered_by", nullable = false)
    private UUID registeredBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MembershipStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
package com.lgsoftworks.attendance.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "attendances",
        indexes = {
                @Index(name = "idx_attendance_gym_check_in", columnList = "gym_id, check_in"),
                @Index(name = "idx_attendance_customer", columnList = "customer_id"),
                @Index(name = "idx_attendance_membership", columnList = "membership_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AttendanceEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "gym_id", nullable = false, updatable = false)
    private UUID gymId;

    @Column(name = "customer_id", nullable = false, updatable = false)
    private UUID customerId;

    @Column(name = "membership_id", nullable = false, updatable = false)
    private UUID membershipId;

    @Column(name = "customer_name", nullable = false, length = 101)
    private String customerName;

    @Column(name = "check_in", nullable = false, updatable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    private LocalDateTime checkOut;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
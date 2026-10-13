package com.lgsoftworks.attendance.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

    private UUID id;
    private UUID customerId;
    private UUID membershipId;
    private String customerName;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private boolean open;
    private LocalDateTime createdAt;
}
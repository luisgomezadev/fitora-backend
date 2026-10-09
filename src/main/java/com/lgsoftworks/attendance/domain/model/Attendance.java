package com.lgsoftworks.attendance.domain.model;

import com.lgsoftworks.attendance.domain.exception.InvalidAttendanceDataException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class Attendance {

    private UUID id;

    private UUID gymId;
    private UUID customerId;
    private UUID membershipId;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    private LocalDateTime createdAt;

    private Attendance(
            UUID gymId,
            UUID customerId,
            UUID membershipId
    ) {
        validateIds(gymId, customerId, membershipId);

        this.id = UUID.randomUUID();
        this.gymId = gymId;
        this.customerId = customerId;
        this.membershipId = membershipId;
        this.checkIn = LocalDateTime.now();
        this.checkOut = null;
        this.createdAt = LocalDateTime.now();
    }

    public static Attendance checkIn(
            UUID gymId,
            UUID customerId,
            UUID membershipId
    ) {
        return new Attendance(
                gymId,
                customerId,
                membershipId
        );
    }

    private void validateIds(
            UUID gymId,
            UUID customerId,
            UUID membershipId
    ) {
        if (gymId == null) {
            throw new InvalidAttendanceDataException("El gimnasio es obligatorio");
        }

        if (customerId == null) {
            throw new InvalidAttendanceDataException("El cliente es obligatorio");
        }

        if (membershipId == null) {
            throw new InvalidAttendanceDataException(
                    "La membresía es obligatoria"
            );
        }
    }

    public void checkOut() {

        if (this.checkOut != null) {
            throw new InvalidAttendanceDataException(
                    "La asistencia ya está cerrada"
            );
        }

        this.checkOut = LocalDateTime.now();
    }

    public boolean isOpen() {
        return checkOut == null;
    }
}
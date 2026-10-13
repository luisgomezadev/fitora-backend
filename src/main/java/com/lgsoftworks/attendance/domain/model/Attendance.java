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

    private String customerName;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    private LocalDateTime createdAt;

    private Attendance(
            UUID gymId,
            UUID customerId,
            UUID membershipId,
            String customerName
    ) {
        validateIds(gymId, customerId, membershipId);
        validateName(customerName);

        this.id = UUID.randomUUID();
        this.gymId = gymId;
        this.customerId = customerId;
        this.membershipId = membershipId;
        this.customerName = customerName;
        this.checkIn = LocalDateTime.now();
        this.checkOut = null;
        this.createdAt = LocalDateTime.now();
    }

    public static Attendance checkIn(
            UUID gymId,
            UUID customerId,
            UUID membershipId,
            String customerName
    ) {
        return new Attendance(
                gymId,
                customerId,
                membershipId,
                customerName
        );
    }

    public static Attendance reconstitute(
            UUID id,
            UUID gymId,
            UUID customerId,
            UUID membershipId,
            String customerName,
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            LocalDateTime createdAt
    ) {
        Attendance attendance = new Attendance();

        attendance.id = id;
        attendance.gymId = gymId;
        attendance.customerId = customerId;
        attendance.membershipId = membershipId;
        attendance.customerName = customerName;
        attendance.checkIn = checkIn;
        attendance.checkOut = checkOut;
        attendance.createdAt = createdAt;

        return attendance;
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

    private void validateName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidAttendanceDataException("El nombre del cliente es obligatorio");
        }
    }

    public void checkOut() {
        checkOut(LocalDateTime.now());
    }

    public void checkOut(LocalDateTime checkOutTime) {

        if (this.checkOut != null) {
            throw new InvalidAttendanceDataException("La asistencia ya está cerrada");
        }

        if (checkOutTime.isBefore(this.checkIn)) {
            throw new InvalidAttendanceDataException(
                    "La salida no puede ser anterior a la entrada"
            );
        }

        this.checkOut = checkOutTime;
    }

    public boolean isOpen() {
        return checkOut == null;
    }
}
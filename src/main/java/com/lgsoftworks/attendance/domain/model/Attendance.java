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
    private UUID studentId;
    private UUID membershipId;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    private LocalDateTime createdAt;

    private Attendance(
            UUID gymId,
            UUID studentId,
            UUID membershipId
    ) {
        validateIds(gymId, studentId, membershipId);

        this.id = UUID.randomUUID();
        this.gymId = gymId;
        this.studentId = studentId;
        this.membershipId = membershipId;
        this.checkIn = LocalDateTime.now();
        this.checkOut = null;
        this.createdAt = LocalDateTime.now();
    }

    public static Attendance checkIn(
            UUID gymId,
            UUID studentId,
            UUID membershipId
    ) {
        return new Attendance(
                gymId,
                studentId,
                membershipId
        );
    }

    private void validateIds(
            UUID gymId,
            UUID studentId,
            UUID membershipId
    ) {
        if (gymId == null) {
            throw new InvalidAttendanceDataException("Gym is required");
        }

        if (studentId == null) {
            throw new InvalidAttendanceDataException("Student is required");
        }

        if (membershipId == null) {
            throw new InvalidAttendanceDataException(
                    "Membership is required"
            );
        }
    }

    public void checkOut() {

        if (this.checkOut != null) {
            throw new InvalidAttendanceDataException(
                    "Attendance is already closed"
            );
        }

        this.checkOut = LocalDateTime.now();
    }

    public boolean isOpen() {
        return checkOut == null;
    }
}
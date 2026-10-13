package com.lgsoftworks.attendance.domain.model;

import java.time.LocalDate;

public record AttendanceSearchCriteria(
        String search,
        LocalDate startDate,
        LocalDate endDate
) {
    public static AttendanceSearchCriteria of(
            String search,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return new AttendanceSearchCriteria(search, startDate, endDate);
    }
}
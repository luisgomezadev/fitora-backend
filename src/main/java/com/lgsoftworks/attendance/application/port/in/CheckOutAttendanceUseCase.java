package com.lgsoftworks.attendance.application.port.in;

import com.lgsoftworks.attendance.application.dto.request.AttendanceRequest;
import com.lgsoftworks.attendance.application.dto.response.AttendanceResponse;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;

public interface CheckOutAttendanceUseCase {

    ApiResponse<AttendanceResponse> checkOut(AttendanceRequest attendanceRequest);
}
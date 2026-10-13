package com.lgsoftworks.attendance.application.port.in;

import com.lgsoftworks.attendance.application.dto.request.AttendanceFilter;
import com.lgsoftworks.attendance.application.dto.response.AttendanceResponse;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;

import java.util.UUID;

public interface GetAttendanceUseCase {

    ApiResponse<AttendanceResponse> findById(UUID id);

    ApiResponse<PageResponse<AttendanceResponse>> findByGymId(
            AttendanceFilter filter, int page, int size);
}
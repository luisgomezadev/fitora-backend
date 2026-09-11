package com.lgsoftworks.student.application.port.in;

import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.student.application.dto.request.StudentUpdateRequest;
import com.lgsoftworks.student.application.dto.response.StudentResponse;

import java.util.UUID;

public interface UpdateStudentUseCase {
    ApiResponse<StudentResponse> update(UUID id, StudentUpdateRequest request);
}

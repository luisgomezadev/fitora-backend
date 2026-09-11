package com.lgsoftworks.student.application.port.in;

import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.student.application.dto.request.StudentRequest;
import com.lgsoftworks.student.application.dto.response.StudentResponse;

public interface CreateStudentUseCase {
    ApiResponse<StudentResponse> execute(StudentRequest studentRequest);
}

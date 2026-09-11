package com.lgsoftworks.student.application.port.in;

import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;
import com.lgsoftworks.student.application.dto.request.StudentFilter;
import com.lgsoftworks.student.application.dto.response.StudentResponse;

import java.util.UUID;

public interface GetStudentUseCase {

    ApiResponse<PageResponse<StudentResponse>> findByGymId(
            StudentFilter filter,
            int page,
            int size
    );

    ApiResponse<StudentResponse> findByIdAndGymId(UUID id);
    ApiResponse<StudentResponse> findByDocumentNumberAndGymId(String documentNumber);
    ApiResponse<StudentResponse> findByEmailAndGymId(String email);
    ApiResponse<StudentResponse> findByPhoneAndGymId(String phone);

}
package com.lgsoftworks.student.infrastructure.adapter.in.web;

import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;
import com.lgsoftworks.student.application.dto.request.StudentFilter;
import com.lgsoftworks.student.application.dto.request.StudentRequest;
import com.lgsoftworks.student.application.dto.request.StudentUpdateRequest;
import com.lgsoftworks.student.application.dto.response.StudentResponse;
import com.lgsoftworks.student.application.port.in.CreateStudentUseCase;
import com.lgsoftworks.student.application.port.in.GetStudentUseCase;
import com.lgsoftworks.student.application.port.in.UpdateStudentUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final CreateStudentUseCase createStudentUseCase;
    private final GetStudentUseCase getStudentUseCase;
    private final UpdateStudentUseCase updateStudentUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(
            @Valid @RequestBody StudentRequest studentRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createStudentUseCase.execute(studentRequest));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable UUID studentId,
            @Valid @RequestBody StudentUpdateRequest studentRequest
    ) {
        return ResponseEntity.ok(updateStudentUseCase.update(studentId, studentRequest));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StudentResponse>>> getAll(
            StudentFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(getStudentUseCase.findByGymId(filter, page, size));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(
            @PathVariable UUID studentId
    ) {
        return ResponseEntity.ok(getStudentUseCase.findByIdAndGymId(studentId));
    }

    @GetMapping("/documentNumber/{documentNumber}")
    public ResponseEntity<ApiResponse<StudentResponse>> getByDocumentNumber(
            @PathVariable String documentNumber
    ) {
        return ResponseEntity.ok(getStudentUseCase.findByDocumentNumberAndGymId(documentNumber));
    }

}

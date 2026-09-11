package com.lgsoftworks.student.application.service;

import com.lgsoftworks.auth.application.service.CurrentUserService;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;
import com.lgsoftworks.student.application.dto.mapper.StudentModelMapper;
import com.lgsoftworks.student.application.dto.request.StudentFilter;
import com.lgsoftworks.student.application.dto.request.StudentRequest;
import com.lgsoftworks.student.application.dto.request.StudentUpdateRequest;
import com.lgsoftworks.student.application.dto.response.StudentResponse;
import com.lgsoftworks.student.application.port.in.CreateStudentUseCase;
import com.lgsoftworks.student.application.port.in.GetStudentUseCase;
import com.lgsoftworks.student.application.port.in.UpdateStudentUseCase;
import com.lgsoftworks.student.domain.exception.StudentByDocumentNumberNotFoundException;
import com.lgsoftworks.student.domain.exception.StudentByEmailNotFoundException;
import com.lgsoftworks.student.domain.exception.StudentByIdNotFoundException;
import com.lgsoftworks.student.domain.exception.StudentByPhoneNotFoundException;
import com.lgsoftworks.student.domain.model.Student;
import com.lgsoftworks.student.domain.model.StudentSearchCriteria;
import com.lgsoftworks.student.domain.port.out.StudentRepositoryPort;
import com.lgsoftworks.student.domain.service.StudentUniquenessValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService implements CreateStudentUseCase,
        GetStudentUseCase, UpdateStudentUseCase {

    private final CurrentUserService currentUserService;
    private final StudentRepositoryPort studentRepositoryPort;
    private final StudentModelMapper studentModelMapper;
    private final StudentUniquenessValidator studentUniquenessValidator;

    @Override
    public ApiResponse<StudentResponse> execute(StudentRequest studentRequest) {

        String email = studentRequest.getEmail();
        String documentNumber = studentRequest.getDocumentNumber();
        String phone = studentRequest.getPhone();

        UUID gymId = currentUserService.getGymId();

        studentUniquenessValidator.validate(
                email,
                documentNumber,
                phone,
                gymId
        );

        Student student = Student.create(
                gymId,
                studentRequest.getDocumentType(),
                documentNumber,
                studentRequest.getFirstName(),
                studentRequest.getLastName(),
                email,
                phone,
                studentRequest.getBirthDate()
        );

        Student savedStudent = studentRepositoryPort.save(student);

        StudentResponse studentResponse =
                studentModelMapper.toResponse(savedStudent);

        return ApiResponse.<StudentResponse>builder()
                .message("Alumno registrado correctamente")
                .data(studentResponse)
                .build();
    }

    @Override
    public ApiResponse<PageResponse<StudentResponse>> findByGymId(
            StudentFilter filter,
            int page,
            int size
    ) {
        UUID gymId = currentUserService.getGymId();

        Pageable pageable = PageRequest.of(page, size);

        StudentSearchCriteria criteria = StudentSearchCriteria.of(
                filter.getSearch(),
                filter.getDocumentType(),
                filter.getStatus()
        );

        Page<Student> students =
                studentRepositoryPort.findByGymId(gymId, criteria, pageable);

        PageResponse<StudentResponse> pageResponse =
                PageResponse.from(students.map(studentModelMapper::toResponse));

        return ApiResponse.<PageResponse<StudentResponse>>builder()
                .message("Alumnos obtenidos correctamente")
                .data(pageResponse)
                .build();
    }

    @Override
    public ApiResponse<StudentResponse> findByIdAndGymId(UUID id) {
        UUID gymId = currentUserService.getGymId();

        StudentResponse studentResponse = studentRepositoryPort
                .findByIdAndGymId(id, gymId)
                .map(studentModelMapper::toResponse)
                .orElseThrow(() -> new StudentByIdNotFoundException(id));

        return ApiResponse.<StudentResponse>builder()
                .message("Alumno por ID obtenido")
                .data(studentResponse)
                .build();
    }

    @Override
    public ApiResponse<StudentResponse> findByDocumentNumberAndGymId(String documentNumber) {
        UUID gymId = currentUserService.getGymId();

        StudentResponse studentResponse = studentRepositoryPort
                .findByDocumentNumberAndGymId(documentNumber, gymId)
                .map(studentModelMapper::toResponse)
                .orElseThrow(() -> new StudentByDocumentNumberNotFoundException(documentNumber));

        return ApiResponse.<StudentResponse>builder()
                .message("Alumno buscado por número de documento obtenido")
                .data(studentResponse)
                .build();
    }

    @Override
    public ApiResponse<StudentResponse> findByEmailAndGymId(String email) {
        UUID gymId = currentUserService.getGymId();

        StudentResponse studentResponse = studentRepositoryPort
                .findByEmailAndGymId(email, gymId)
                .map(studentModelMapper::toResponse)
                .orElseThrow(() -> new StudentByEmailNotFoundException(email));

        return ApiResponse.<StudentResponse>builder()
                .message("Alumno buscado por correo obtenido")
                .data(studentResponse)
                .build();
    }

    @Override
    public ApiResponse<StudentResponse> findByPhoneAndGymId(String phone) {
        UUID gymId = currentUserService.getGymId();

        StudentResponse studentResponse = studentRepositoryPort
                .findByPhoneAndGymId(phone, gymId)
                .map(studentModelMapper::toResponse)
                .orElseThrow(() -> new StudentByPhoneNotFoundException(phone));

        return ApiResponse.<StudentResponse>builder()
                .message("Alumno buscado por número de celular obtenido")
                .data(studentResponse)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<StudentResponse> update(UUID id, StudentUpdateRequest studentRequest) {
        UUID gymId = currentUserService.getGymId();

        Student student = studentRepositoryPort
                .findByIdAndGymId(id, gymId)
                .orElseThrow(() -> new StudentByIdNotFoundException(id));

        student.updateInformation(
                studentRequest.getFirstName(),
                studentRequest.getLastName(),
                studentRequest.getEmail(),
                studentRequest.getPhone(),
                studentRequest.getBirthDate()
        );

        Student updatedStudent = studentRepositoryPort.save(student);

        StudentResponse studentResponse = studentModelMapper.toResponse(updatedStudent);

        return ApiResponse.<StudentResponse>builder()
                .message("Alumno actualizado correctamente")
                .data(studentResponse)
                .build();
    }
}
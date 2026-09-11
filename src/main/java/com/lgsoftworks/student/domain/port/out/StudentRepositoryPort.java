package com.lgsoftworks.student.domain.port.out;

import com.lgsoftworks.student.domain.model.Student;
import com.lgsoftworks.student.domain.model.StudentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepositoryPort {

    Student save(Student student);

    Optional<Student> findByIdAndGymId(UUID id, UUID gymId);

    Optional<Student> findByDocumentNumberAndGymId(String documentNumber, UUID gymId);

    Optional<Student> findByEmailAndGymId(String email, UUID gymId);

    Optional<Student> findByPhoneAndGymId(String phone, UUID gymId);

    Page<Student> findByGymId(
            UUID gymId,
            StudentSearchCriteria criteria,
            Pageable pageable
    );

    boolean existsByDocumentNumberAndGymId(String documentNumber, UUID gymId);

    boolean existsByEmailAndGymId(String email, UUID gymId);

    boolean existsByPhoneAndGymId(String phone, UUID gymId);

}

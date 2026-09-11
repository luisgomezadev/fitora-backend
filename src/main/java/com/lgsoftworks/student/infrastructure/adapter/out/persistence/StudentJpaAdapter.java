package com.lgsoftworks.student.infrastructure.adapter.out.persistence;

import com.lgsoftworks.student.application.dto.request.StudentFilter;
import com.lgsoftworks.student.domain.model.Student;
import com.lgsoftworks.student.domain.model.StudentSearchCriteria;
import com.lgsoftworks.student.domain.port.out.StudentRepositoryPort;
import com.lgsoftworks.student.infrastructure.adapter.out.persistence.entity.StudentEntity;
import com.lgsoftworks.student.infrastructure.adapter.out.persistence.mapper.StudentDboMapper;
import com.lgsoftworks.student.infrastructure.adapter.out.persistence.repository.StudentRepository;
import com.lgsoftworks.student.infrastructure.adapter.out.persistence.specification.StudentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StudentJpaAdapter implements StudentRepositoryPort {

    private final StudentRepository studentRepository;
    private final StudentDboMapper studentDboMapper;

    @Override
    public Student save(Student student) {
        StudentEntity studentEntity = studentRepository.save(studentDboMapper.toEntity(student));
        return studentDboMapper.toDomain(studentEntity);
    }

    @Override
    public Optional<Student> findByIdAndGymId(UUID id, UUID gymId) {
        return studentRepository
                .findByIdAndGymId(id, gymId)
                .map(studentDboMapper::toDomain);
    }

    @Override
    public Optional<Student> findByDocumentNumberAndGymId(String documentNumber, UUID gymId) {
        return studentRepository
                .findByDocumentNumberAndGymId(documentNumber, gymId)
                .map(studentDboMapper::toDomain);
    }

    @Override
    public Optional<Student> findByEmailAndGymId(String email, UUID gymId) {
        return studentRepository
                .findByEmailAndGymId(email, gymId)
                .map(studentDboMapper::toDomain);
    }

    @Override
    public Optional<Student> findByPhoneAndGymId(String phone, UUID gymId) {
        return studentRepository
                .findByPhoneAndGymId(phone, gymId)
                .map(studentDboMapper::toDomain);
    }

    @Override
    public Page<Student> findByGymId(UUID gymId, StudentSearchCriteria criteria, Pageable pageable) {
        Specification<StudentEntity> spec = StudentSpecification.withCriteria(gymId, criteria);
        Page<StudentEntity> entities = studentRepository.findAll(spec, pageable);
        return entities.map(studentDboMapper::toDomain);
    }

    @Override
    public boolean existsByDocumentNumberAndGymId(String documentNumber, UUID gymId) {
        return studentRepository.existsByDocumentNumberAndGymId(documentNumber, gymId);
    }

    @Override
    public boolean existsByEmailAndGymId(String email, UUID gymId) {
        return studentRepository.existsByEmailAndGymId(email, gymId);
    }

    @Override
    public boolean existsByPhoneAndGymId(String phone, UUID gymId) {
        return studentRepository.existsByPhoneAndGymId(phone, gymId);
    }
}
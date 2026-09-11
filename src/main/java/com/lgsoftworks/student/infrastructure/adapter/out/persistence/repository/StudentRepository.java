package com.lgsoftworks.student.infrastructure.adapter.out.persistence.repository;

import com.lgsoftworks.student.infrastructure.adapter.out.persistence.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends
        JpaRepository<StudentEntity, UUID>,
        JpaSpecificationExecutor<StudentEntity> {

    Optional<StudentEntity> findByIdAndGymId(UUID id, UUID gymId);
    Optional<StudentEntity> findByEmailAndGymId(String email, UUID gymId);
    Optional<StudentEntity> findByDocumentNumberAndGymId(String documentNumber, UUID gymId);
    Optional<StudentEntity> findByPhoneAndGymId(String phone, UUID gymId);

    boolean existsByEmailAndGymId(String email, UUID gymId);
    boolean existsByDocumentNumberAndGymId(String documentNumber, UUID gymId);
    boolean existsByPhoneAndGymId(String phone, UUID gymId);
}
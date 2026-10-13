package com.lgsoftworks.attendance.infrastructure.adapter.out.persistence.repository;

import com.lgsoftworks.attendance.infrastructure.adapter.out.persistence.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository
        extends JpaRepository<AttendanceEntity, UUID>, JpaSpecificationExecutor<AttendanceEntity> {

    Optional<AttendanceEntity> findByIdAndGymId(UUID id, UUID gymId);

    boolean existsByCustomerIdAndGymIdAndCheckOutIsNull(UUID customerId, UUID gymId);

    Optional<AttendanceEntity> findByCustomerIdAndGymIdAndCheckOutIsNull(UUID customerId, UUID gymId);

    List<AttendanceEntity> findByCheckOutIsNullAndCheckInBefore(LocalDateTime threshold);

}
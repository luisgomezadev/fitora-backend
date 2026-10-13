package com.lgsoftworks.attendance.infrastructure.adapter.out.persistence;

import com.lgsoftworks.attendance.domain.model.Attendance;
import com.lgsoftworks.attendance.domain.model.AttendanceSearchCriteria;
import com.lgsoftworks.attendance.domain.port.out.AttendanceRepositoryPort;
import com.lgsoftworks.attendance.infrastructure.adapter.out.persistence.entity.AttendanceEntity;
import com.lgsoftworks.attendance.infrastructure.adapter.out.persistence.mapper.AttendanceDboMapper;
import com.lgsoftworks.attendance.infrastructure.adapter.out.persistence.repository.AttendanceRepository;
import com.lgsoftworks.attendance.infrastructure.adapter.out.persistence.specification.AttendanceSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AttendanceJpaAdapter implements AttendanceRepositoryPort {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceDboMapper attendanceDboMapper;

    @Override
    public Attendance save(Attendance attendance) {
        AttendanceEntity entity = attendanceRepository.save(attendanceDboMapper.toEntity(attendance));
        return attendanceDboMapper.toDomain(entity);
    }

    @Override
    public Optional<Attendance> findByIdAndGymId(UUID id, UUID gymId) {
        return attendanceRepository
                .findByIdAndGymId(id, gymId)
                .map(attendanceDboMapper::toDomain);
    }

    @Override
    public boolean existsOpenByCustomerIdAndGymId(UUID customerId, UUID gymId) {
        return attendanceRepository.existsByCustomerIdAndGymIdAndCheckOutIsNull(customerId, gymId);
    }

    @Override
    public Page<Attendance> findByGymId(UUID gymId, AttendanceSearchCriteria criteria, Pageable pageable) {
        Specification<AttendanceEntity> spec = AttendanceSpecification.withCriteria(gymId, criteria);
        Page<AttendanceEntity> entities = attendanceRepository.findAll(spec, pageable);
        return entities.map(attendanceDboMapper::toDomain);
    }

    @Override
    public Optional<Attendance> findOpenByCustomerIdAndGymId(UUID customerId, UUID gymId) {
        return attendanceRepository
                .findByCustomerIdAndGymIdAndCheckOutIsNull(customerId, gymId)
                .map(attendanceDboMapper::toDomain);
    }

    @Override
    public List<Attendance> findOpenCheckedInBefore(LocalDateTime threshold) {
        return attendanceRepository
                .findByCheckOutIsNullAndCheckInBefore(threshold)
                .stream()
                .map(attendanceDboMapper::toDomain)
                .toList();
    }
}
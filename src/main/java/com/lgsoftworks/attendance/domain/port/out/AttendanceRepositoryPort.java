package com.lgsoftworks.attendance.domain.port.out;

import com.lgsoftworks.attendance.domain.model.Attendance;
import com.lgsoftworks.attendance.domain.model.AttendanceSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepositoryPort {

    Attendance save(Attendance attendance);

    Optional<Attendance> findByIdAndGymId(UUID id, UUID gymId);

    Optional<Attendance> findOpenByCustomerIdAndGymId(UUID customerId, UUID gymId);

    boolean existsOpenByCustomerIdAndGymId(UUID customerId, UUID gymId);

    List<Attendance> findOpenCheckedInBefore(LocalDateTime threshold);

    Page<Attendance> findByGymId(
            UUID gymId,
            AttendanceSearchCriteria criteria,
            Pageable pageable
    );
}
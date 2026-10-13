package com.lgsoftworks.attendance.infrastructure.adapter.out.persistence.mapper;

import com.lgsoftworks.attendance.domain.model.Attendance;
import com.lgsoftworks.attendance.infrastructure.adapter.out.persistence.entity.AttendanceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AttendanceDboMapper {

    AttendanceEntity toEntity(Attendance attendance);

    default Attendance toDomain(AttendanceEntity entity) {

        return Attendance.reconstitute(
                entity.getId(),
                entity.getGymId(),
                entity.getCustomerId(),
                entity.getMembershipId(),
                entity.getCustomerName(),
                entity.getCheckIn(),
                entity.getCheckOut(),
                entity.getCreatedAt()
        );
    }
}
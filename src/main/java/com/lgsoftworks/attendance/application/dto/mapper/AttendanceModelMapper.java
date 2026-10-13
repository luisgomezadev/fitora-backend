package com.lgsoftworks.attendance.application.dto.mapper;

import com.lgsoftworks.attendance.application.dto.response.AttendanceResponse;
import com.lgsoftworks.attendance.domain.model.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AttendanceModelMapper {

    AttendanceResponse toResponse(Attendance attendance);
}
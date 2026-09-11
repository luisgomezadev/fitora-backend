package com.lgsoftworks.student.application.dto.mapper;

import com.lgsoftworks.student.application.dto.response.StudentResponse;
import com.lgsoftworks.student.domain.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface StudentModelMapper {

    @Mapping(target = "fullName", expression = "java(student.getFullName())")
    StudentResponse toResponse(Student student);
}

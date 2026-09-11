package com.lgsoftworks.student.infrastructure.adapter.out.persistence.mapper;

import com.lgsoftworks.student.domain.model.Student;
import com.lgsoftworks.student.infrastructure.adapter.out.persistence.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface StudentDboMapper {

    StudentEntity toEntity(Student student);

    default Student toDomain(StudentEntity entity) {

        return Student.reconstitute(
                entity.getId(),
                entity.getGymId(),
                entity.getDocumentType(),
                entity.getDocumentNumber(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getBirthDate(),
                entity.getStatus(),
                entity.getRegistrationDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}

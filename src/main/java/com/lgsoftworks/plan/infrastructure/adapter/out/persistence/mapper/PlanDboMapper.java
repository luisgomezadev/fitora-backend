package com.lgsoftworks.plan.infrastructure.adapter.out.persistence.mapper;

import com.lgsoftworks.plan.domain.model.Plan;
import com.lgsoftworks.plan.infrastructure.adapter.out.persistence.entity.PlanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PlanDboMapper {

    PlanEntity toEntity(Plan plan);

    default Plan toDomain(PlanEntity entity) {

        return Plan.reconstitute(
                entity.getId(),
                entity.getGymId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDurationDays(),
                entity.getPrice(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
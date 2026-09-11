package com.lgsoftworks.gym.infrastructure.adapter.out.persistence.mapper;

import com.lgsoftworks.gym.domain.model.Gym;
import com.lgsoftworks.gym.infrastructure.adapter.out.persistence.entity.GymEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface GymDboMapper {

    GymEntity toEntity(Gym gym);

    default Gym toDomain(GymEntity entity) {

        return Gym.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getDocument(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddress(),
                entity.getCity(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
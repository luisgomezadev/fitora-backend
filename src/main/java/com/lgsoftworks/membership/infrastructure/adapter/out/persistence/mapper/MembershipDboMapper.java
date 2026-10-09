package com.lgsoftworks.membership.infrastructure.adapter.out.persistence.mapper;

import com.lgsoftworks.membership.domain.model.Membership;
import com.lgsoftworks.membership.infrastructure.adapter.out.persistence.entity.MembershipEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MembershipDboMapper {

    MembershipEntity toEntity(Membership membership);

    default Membership toDomain(MembershipEntity entity) {

        return Membership.reconstitute(
                entity.getId(),
                entity.getGymId(),
                entity.getCustomerId(),
                entity.getPlanId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPrice(),
                entity.getPaymentMethod(),
                entity.getPaymentDate(),
                entity.getRegisteredBy(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
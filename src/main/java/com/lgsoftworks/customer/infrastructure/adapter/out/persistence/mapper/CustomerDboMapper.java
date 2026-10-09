package com.lgsoftworks.customer.infrastructure.adapter.out.persistence.mapper;

import com.lgsoftworks.customer.domain.model.Customer;
import com.lgsoftworks.customer.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CustomerDboMapper {

    CustomerEntity toEntity(Customer customer);

    default Customer toDomain(CustomerEntity entity) {

        return Customer.reconstitute(
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

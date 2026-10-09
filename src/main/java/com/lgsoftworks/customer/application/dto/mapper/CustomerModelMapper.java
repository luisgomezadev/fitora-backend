package com.lgsoftworks.customer.application.dto.mapper;

import com.lgsoftworks.customer.application.dto.response.CustomerResponse;
import com.lgsoftworks.customer.domain.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CustomerModelMapper {

    @Mapping(target = "fullName", expression = "java(customer.getFullName())")
    CustomerResponse toResponse(Customer customer);
}

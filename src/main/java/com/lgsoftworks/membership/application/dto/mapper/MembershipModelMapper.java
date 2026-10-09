package com.lgsoftworks.membership.application.dto.mapper;

import com.lgsoftworks.customer.domain.model.Customer;
import com.lgsoftworks.membership.application.dto.response.MembershipResponse;
import com.lgsoftworks.membership.domain.model.Membership;
import com.lgsoftworks.plan.domain.model.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MembershipModelMapper {

    @Mapping(target = "id", source = "membership.id")
    @Mapping(target = "customerId", source = "membership.customerId")
    @Mapping(target = "customerFullName", source = "customer.fullName")
    @Mapping(target = "planId", source = "membership.planId")
    @Mapping(target = "planName", source = "plan.name")
    @Mapping(target = "startDate", source = "membership.startDate")
    @Mapping(target = "endDate", source = "membership.endDate")
    @Mapping(target = "price", source = "membership.price")
    @Mapping(target = "paymentMethod", source = "membership.paymentMethod")
    @Mapping(target = "paymentDate", source = "membership.paymentDate")
    @Mapping(target = "status", source = "membership.status")
    MembershipResponse toResponse(Membership membership, Customer customer, Plan plan);
}
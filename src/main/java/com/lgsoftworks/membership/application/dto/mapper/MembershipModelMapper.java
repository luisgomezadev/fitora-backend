package com.lgsoftworks.membership.application.dto.mapper;

import com.lgsoftworks.membership.application.dto.response.MembershipResponse;
import com.lgsoftworks.membership.domain.model.Membership;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MembershipModelMapper {

    MembershipResponse toResponse(Membership membership);
}
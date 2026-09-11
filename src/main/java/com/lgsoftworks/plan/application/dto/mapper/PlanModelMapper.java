package com.lgsoftworks.plan.application.dto.mapper;

import com.lgsoftworks.plan.application.dto.response.PlanResponse;
import com.lgsoftworks.plan.domain.model.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PlanModelMapper {

    PlanResponse toResponse(Plan plan);

}

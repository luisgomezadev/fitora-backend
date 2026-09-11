package com.lgsoftworks.gym.application.dto.mapper;

import com.lgsoftworks.gym.application.dto.response.GymResponse;
import com.lgsoftworks.gym.domain.model.Gym;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface GymModelMapper {

    GymResponse toResponse(Gym gym);

}
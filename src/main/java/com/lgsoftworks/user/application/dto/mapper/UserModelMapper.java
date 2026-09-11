package com.lgsoftworks.user.application.dto.mapper;

import com.lgsoftworks.user.application.dto.response.UserResponse;
import com.lgsoftworks.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserModelMapper {

    UserResponse toResponse(User user);

}

package com.lgsoftworks.user.infrastructure.adapter.out.persistence.mapper;

import com.lgsoftworks.user.domain.model.User;
import com.lgsoftworks.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserDboMapper {

    UserEntity toEntity(User user);

    default User toDomain(UserEntity entity) {

        return User.reconstitute(
                entity.getId(),
                entity.getGymId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.getStatus(),
                entity.isEmailVerified(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
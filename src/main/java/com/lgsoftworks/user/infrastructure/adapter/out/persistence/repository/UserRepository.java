package com.lgsoftworks.user.infrastructure.adapter.out.persistence.repository;

import com.lgsoftworks.user.domain.enums.UserRole;
import com.lgsoftworks.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByIdAndGymId(UUID id, UUID gymId);
    Optional<UserEntity> findByEmailAndGymId(String email, UUID gymId);
    Optional<UserEntity> findByUsernameAndGymId(String username, UUID gymId);
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByGymIdAndRole(UUID gymId, UserRole userRole);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}

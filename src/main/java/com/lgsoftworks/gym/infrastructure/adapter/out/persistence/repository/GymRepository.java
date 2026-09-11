package com.lgsoftworks.gym.infrastructure.adapter.out.persistence.repository;

import com.lgsoftworks.gym.infrastructure.adapter.out.persistence.entity.GymEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GymRepository extends JpaRepository<GymEntity, UUID> {

    Optional<GymEntity> findByDocument(String document);

    Optional<GymEntity> findByEmail(String email);

    boolean existsByDocument(String document);

    boolean existsByEmail(String email);
}
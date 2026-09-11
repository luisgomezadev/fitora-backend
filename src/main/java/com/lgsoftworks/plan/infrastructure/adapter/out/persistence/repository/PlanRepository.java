package com.lgsoftworks.plan.infrastructure.adapter.out.persistence.repository;

import com.lgsoftworks.plan.domain.enums.PlanStatus;
import com.lgsoftworks.plan.infrastructure.adapter.out.persistence.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, UUID> {

    Optional<PlanEntity> findByIdAndGymId(UUID id, UUID gymId);

    List<PlanEntity> findByGymIdAndStatus(UUID gymId, PlanStatus status);

    boolean existsByNameAndGymId(String name, UUID gymId);
}
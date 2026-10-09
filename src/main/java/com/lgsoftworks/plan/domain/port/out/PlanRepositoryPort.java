package com.lgsoftworks.plan.domain.port.out;

import com.lgsoftworks.plan.domain.model.Plan;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlanRepositoryPort {

    Plan save(Plan plan);

    Optional<Plan> findByIdAndGymId(UUID id, UUID gymId);

    List<Plan> findAllByIdInAndGymId(Collection<UUID> ids, UUID gymId);

    List<Plan> findByGymId(UUID gymId);

    boolean existsByNameAndGymId(String planName, UUID gymId);

}

package com.lgsoftworks.plan.infrastructure.adapter.out.persistence;

import com.lgsoftworks.plan.domain.enums.PlanStatus;
import com.lgsoftworks.plan.domain.model.Plan;
import com.lgsoftworks.plan.domain.port.out.PlanRepositoryPort;
import com.lgsoftworks.plan.infrastructure.adapter.out.persistence.entity.PlanEntity;
import com.lgsoftworks.plan.infrastructure.adapter.out.persistence.mapper.PlanDboMapper;
import com.lgsoftworks.plan.infrastructure.adapter.out.persistence.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlanJpaAdapter implements PlanRepositoryPort {

    private final PlanRepository planRepository;
    private final PlanDboMapper planDboMapper;

    @Override
    public Plan save(Plan plan) {
        PlanEntity planEntity = planRepository.save(planDboMapper.toEntity(plan));
        return planDboMapper.toDomain(planEntity);
    }

    @Override
    public Optional<Plan> findByIdAndGymId(UUID id, UUID gymId) {
        return planRepository.findByIdAndGymId(id, gymId)
                .map(planDboMapper::toDomain);
    }

    @Override
    public List<Plan> findAllByIdInAndGymId(Collection<UUID> ids, UUID gymId) {
        return planRepository.findAllByIdInAndGymId(ids, gymId)
                .stream()
                .map(planDboMapper::toDomain)
                .toList();
    }

    @Override
    public List<Plan> findByGymId(UUID gymId) {
        return planRepository.findByGymIdAndStatus(gymId, PlanStatus.ACTIVE)
                .stream()
                .map(planDboMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByNameAndGymId(String name, UUID gymId) {
        return planRepository.existsByNameAndGymId(name, gymId);
    }
}
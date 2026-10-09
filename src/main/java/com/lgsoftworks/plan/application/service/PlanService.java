package com.lgsoftworks.plan.application.service;

import com.lgsoftworks.auth.application.service.CurrentUserService;
import com.lgsoftworks.plan.application.dto.mapper.PlanModelMapper;
import com.lgsoftworks.plan.application.dto.request.PlanRequest;
import com.lgsoftworks.plan.application.dto.response.PlanResponse;
import com.lgsoftworks.plan.application.port.in.CreatePlanUseCase;
import com.lgsoftworks.plan.application.port.in.GetPlanUseCase;
import com.lgsoftworks.plan.application.port.in.UpdatePlanUseCase;
import com.lgsoftworks.plan.domain.exception.PlanByIdNotFoundException;
import com.lgsoftworks.plan.domain.model.Plan;
import com.lgsoftworks.plan.domain.port.out.PlanRepositoryPort;
import com.lgsoftworks.plan.domain.service.PlanUniquenessValidator;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanService implements CreatePlanUseCase,
        GetPlanUseCase, UpdatePlanUseCase {

    private final PlanRepositoryPort planRepositoryPort;
    private final CurrentUserService currentUserService;
    private final PlanUniquenessValidator planUniquenessValidator;
    private final PlanModelMapper planModelMapper;

    @Override
    public ApiResponse<PlanResponse> execute(PlanRequest planRequest) {

        UUID gymId = currentUserService.getGymId();

        planUniquenessValidator.validate(planRequest.getName(), gymId);

        Plan plan = Plan.create(
                gymId,
                planRequest.getName(),
                planRequest.getDescription(),
                planRequest.getDurationDays(),
                planRequest.getPrice()
        );

        Plan planSaved = planRepositoryPort.save(plan);

        PlanResponse planResponse = planModelMapper.toResponse(planSaved);

        return ApiResponse.<PlanResponse>builder()
                .message("Plan creado correctamente")
                .data(planResponse)
                .build();
    }

    @Override
    public ApiResponse<List<PlanResponse>> findByGymId() {

        UUID gymId = currentUserService.getGymId();

        List<PlanResponse> planResponses = planRepositoryPort
                .findByGymId(gymId)
                .stream()
                .map(planModelMapper::toResponse)
                .toList();

        return ApiResponse.<List<PlanResponse>>builder()
                .message("Planes obtenidos correctamente")
                .data(planResponses)
                .build();
    }

    @Override
    public ApiResponse<PlanResponse> findByIdAndGymId(UUID planId) {
        UUID gymId = currentUserService.getGymId();

        PlanResponse planResponse = planRepositoryPort
                .findByIdAndGymId(planId, gymId)
                .map(planModelMapper::toResponse)
                .orElseThrow(PlanByIdNotFoundException::new);

        return ApiResponse.<PlanResponse>builder()
                .message("Plan obtenido correctamente")
                .data(planResponse)
                .build();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PlanResponse> update(UUID planId, PlanRequest planRequest) {

        UUID gymId = currentUserService.getGymId();

        Plan plan = planRepositoryPort
                .findByIdAndGymId(planId, gymId)
                .orElseThrow(PlanByIdNotFoundException::new);

        plan.update(
                planRequest.getName(),
                planRequest.getDescription(),
                planRequest.getDurationDays(),
                planRequest.getPrice()
        );

        Plan updatedPlan = planRepositoryPort.save(plan);

        PlanResponse planResponse = planModelMapper.toResponse(updatedPlan);

        return ApiResponse.<PlanResponse>builder()
                .message("Plan actualizado correctamente")
                .data(planResponse)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deactivate(UUID planId) {
        UUID gymId = currentUserService.getGymId();

        Plan plan = planRepositoryPort
                .findByIdAndGymId(planId, gymId)
                .orElseThrow(PlanByIdNotFoundException::new);

        plan.deactivate();
        planRepositoryPort.save(plan);
        return ApiResponse.<Void>builder()
                .message("Plan desactivado correctamente")
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> activate(UUID planId) {
        UUID gymId = currentUserService.getGymId();

        Plan plan = planRepositoryPort
                .findByIdAndGymId(planId, gymId)
                .orElseThrow(PlanByIdNotFoundException::new);

        plan.activate();
        planRepositoryPort.save(plan);
        return ApiResponse.<Void>builder()
                .message("Plan activado correctamente")
                .build();
    }
}

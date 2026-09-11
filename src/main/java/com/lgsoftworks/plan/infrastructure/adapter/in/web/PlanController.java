package com.lgsoftworks.plan.infrastructure.adapter.in.web;

import com.lgsoftworks.plan.application.dto.request.PlanRequest;
import com.lgsoftworks.plan.application.dto.response.PlanResponse;
import com.lgsoftworks.plan.application.port.in.CreatePlanUseCase;
import com.lgsoftworks.plan.application.port.in.GetPlanUseCase;
import com.lgsoftworks.plan.application.port.in.UpdatePlanUseCase;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
@Validated
public class PlanController {

    private final CreatePlanUseCase createPlanUseCase;
    private final GetPlanUseCase getPlanUseCase;
    private final UpdatePlanUseCase updatePlanUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<PlanResponse>> createPlan(
            @Valid @RequestBody PlanRequest planRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createPlanUseCase.execute(planRequest));
    }

    @PutMapping("/{planId}")
    public ResponseEntity<ApiResponse<PlanResponse>> updatePlan(
            @PathVariable UUID planId,
            @Valid @RequestBody PlanRequest planRequest
    ) {
        return ResponseEntity.ok(updatePlanUseCase.update(planId, planRequest));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlanResponse>>> getAll() {
        return ResponseEntity.ok(getPlanUseCase.findByGymId());
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivatePlanById(@PathVariable UUID id) {
        return ResponseEntity.ok(updatePlanUseCase.deactivate(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activatePlanById(@PathVariable UUID id) {
        return ResponseEntity.ok(updatePlanUseCase.activate(id));
    }
}
package com.lgsoftworks.plan.application.port.in;

import com.lgsoftworks.plan.application.dto.response.PlanResponse;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface GetPlanUseCase {

    ApiResponse<List<PlanResponse>> findByGymId();
    ApiResponse<PlanResponse> findByIdAndGymId(UUID planId);

}

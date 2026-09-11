package com.lgsoftworks.plan.application.port.in;

import com.lgsoftworks.plan.application.dto.request.PlanRequest;
import com.lgsoftworks.plan.application.dto.response.PlanResponse;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;

import java.util.UUID;

public interface UpdatePlanUseCase {

    ApiResponse<PlanResponse> update(UUID planId, PlanRequest planRequest);
    ApiResponse<Void> deactivate(UUID planId);
    ApiResponse<Void> activate(UUID planId);

}

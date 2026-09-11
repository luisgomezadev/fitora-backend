package com.lgsoftworks.plan.application.port.in;

import com.lgsoftworks.plan.application.dto.request.PlanRequest;
import com.lgsoftworks.plan.application.dto.response.PlanResponse;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;

public interface CreatePlanUseCase {

    ApiResponse<PlanResponse> execute(PlanRequest planRequest);

}

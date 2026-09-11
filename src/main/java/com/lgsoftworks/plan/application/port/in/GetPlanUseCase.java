package com.lgsoftworks.plan.application.port.in;

import com.lgsoftworks.plan.application.dto.response.PlanResponse;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;

import java.util.List;

public interface GetPlanUseCase {

    ApiResponse<List<PlanResponse>> findByGymId();

}

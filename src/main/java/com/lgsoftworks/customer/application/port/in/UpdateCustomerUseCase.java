package com.lgsoftworks.customer.application.port.in;

import com.lgsoftworks.customer.application.dto.request.CustomerUpdateRequest;
import com.lgsoftworks.customer.application.dto.response.CustomerResponse;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;

import java.util.UUID;

public interface UpdateCustomerUseCase {
    ApiResponse<CustomerResponse> update(UUID id, CustomerUpdateRequest request);
    ApiResponse<Void> deactivate(UUID customerId);
    ApiResponse<Void> activate(UUID customerId);
    ApiResponse<Void> suspend(UUID customerId);
}

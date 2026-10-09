package com.lgsoftworks.customer.application.port.in;

import com.lgsoftworks.customer.application.dto.request.CustomerRequest;
import com.lgsoftworks.customer.application.dto.response.CustomerResponse;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;

public interface CreateCustomerUseCase {
    ApiResponse<CustomerResponse> execute(CustomerRequest customerRequest);
}

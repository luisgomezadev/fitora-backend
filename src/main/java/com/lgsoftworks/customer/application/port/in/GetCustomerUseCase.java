package com.lgsoftworks.customer.application.port.in;

import com.lgsoftworks.customer.application.dto.request.CustomerFilter;
import com.lgsoftworks.customer.application.dto.response.CustomerResponse;
import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.shared.application.dto.response.PageResponse;

import java.util.UUID;

public interface GetCustomerUseCase {

    ApiResponse<PageResponse<CustomerResponse>> findByGymId(
            CustomerFilter filter,
            int page,
            int size
    );

    ApiResponse<CustomerResponse> findByIdAndGymId(UUID id);
    ApiResponse<CustomerResponse> findByDocumentNumberAndGymId(String documentNumber);
    ApiResponse<CustomerResponse> findByEmailAndGymId(String email);
    ApiResponse<CustomerResponse> findByPhoneAndGymId(String phone);

}
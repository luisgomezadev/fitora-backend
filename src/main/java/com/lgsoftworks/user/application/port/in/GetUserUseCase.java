package com.lgsoftworks.user.application.port.in;

import com.lgsoftworks.shared.application.dto.response.ApiResponse;
import com.lgsoftworks.user.application.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface  GetUserUseCase {
    ApiResponse<UserResponse> findByIdAndGymId(UUID id);
    ApiResponse<UserResponse> findByEmailAndGymId(String email);
    ApiResponse<UserResponse> findByUsernameAndGymId(String username);
    ApiResponse<List<UserResponse>> findByGymId();
}
